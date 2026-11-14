const { test, before, after } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { createApp } = require('./server');

let server;
let baseUrl;
let tempDbPath;

before(async () => {
  const tempDir = fs.mkdtempSync(path.join(os.tmpdir(), 'sitemitra-test-'));
  tempDbPath = path.join(tempDir, 'test_db.json');
  server = createApp(tempDbPath);
  await new Promise((resolve) => server.listen(0, '127.0.0.1', resolve));
  const addr = server.address();
  baseUrl = `http://127.0.0.1:${addr.port}`;
});

after(async () => {
  await new Promise((resolve) => server.close(resolve));
  if (tempDbPath && fs.existsSync(tempDbPath)) {
    fs.rmSync(path.dirname(tempDbPath), { recursive: true, force: true });
  }
});

test('GET /api/health returns ok and seeded counts', async () => {
  const res = await fetch(`${baseUrl}/api/health`);
  assert.equal(res.status, 200);
  const data = await res.json();
  assert.equal(data.status, 'ok');
  assert.equal(data.service, 'SiteMitra Data Server');
  assert.equal(data.counts.users, 4);
  assert.equal(data.counts.sites, 6);
  assert.equal(data.counts.jobs, 8);
});

test('POST /api/jobs creates and persists a job', async () => {
  const res = await fetch(`${baseUrl}/api/jobs`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      title: 'Tile Masons for Mall Atrium',
      category: 'Mason',
      wagePerDay: 1150,
      vacancies: 4,
      siteName: 'Phoenix Marketcity Phase 2',
      area: 'Whitefield',
    }),
  });
  assert.equal(res.status, 201);
  const created = await res.json();
  assert.ok(created.id > 8);
  assert.equal(created.title, 'Tile Masons for Mall Atrium');

  const listRes = await fetch(`${baseUrl}/api/jobs?area=Whitefield`);
  assert.equal(listRes.status, 200);
  const list = await listRes.json();
  assert.ok(list.some((j) => j.id === created.id));
});

test('POST and PATCH /api/applications updates hiredCount on HIRED', async () => {
  const postRes = await fetch(`${baseUrl}/api/applications`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      jobId: 1,
      workerId: 1,
      workerName: 'Test Worker',
      workerPhone: '+91 90000 11111',
      workerSkill: 'Mason',
    }),
  });
  assert.equal(postRes.status, 201);
  const appRecord = await postRes.json();
  assert.equal(appRecord.status, 'APPLIED');

  const patchRes = await fetch(`${baseUrl}/api/applications/${appRecord.id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ status: 'HIRED' }),
  });
  assert.equal(patchRes.status, 200);
  const updatedApp = await patchRes.json();
  assert.equal(updatedApp.status, 'HIRED');

  const jobsRes = await fetch(`${baseUrl}/api/jobs`);
  const jobs = await jobsRes.json();
  const job1 = jobs.find((j) => j.id === 1);
  assert.equal(job1.hiredCount, 6);
});

test('POST /api/sync merges records and returns full snapshot', async () => {
  const syncRes = await fetch(`${baseUrl}/api/sync`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      sites: [
        {
          id: 99,
          name: 'Kengeri Metro Hub',
          developer: 'BMRCL',
          address: 'Mysore Road, Kengeri',
          area: 'Kengeri',
          latitude: 12.917,
          longitude: 77.483,
        },
      ],
    }),
  });
  assert.equal(syncRes.status, 200);
  const snapshot = await syncRes.json();
  assert.ok(snapshot.sites.some((s) => s.id === 99 && s.name === 'Kengeri Metro Hub'));
  assert.ok(snapshot.syncedAt);
});
