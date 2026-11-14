const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = Number(process.env.PORT || 3000);
const ROOT = path.resolve(__dirname);
const DATA_DIR = path.join(ROOT, 'server-data');
const DB_FILE = process.env.SITEMITRA_DB_FILE || path.join(DATA_DIR, 'sitemitra_db.json');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css',
  '.js': 'application/javascript',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.apk': 'application/vnd.android.package-archive',
};

function getSeedData() {
  return {
    users: [
      {
        id: 1,
        name: 'Ramesh Kumar (ಕಾರ್ಮಿಕ)',
        phone: '+91 98450 78210',
        role: 'WORKER',
        language: 'en',
        status: 'VERIFIED',
        skills: 'Mason, Tile Laying, Brickwork',
        experienceYears: 6,
        expectedWage: 950,
        isAvailable: true,
        rating: 4.9,
        completedJobsCount: 28,
        city: 'Bengaluru',
      },
      {
        id: 2,
        name: 'Mestri Govindappa (ಮೇಸ್ತ್ರಿ)',
        phone: '+91 98451 22340',
        role: 'SUPERVISOR',
        language: 'en',
        status: 'VERIFIED',
        skills: 'Site Supervision, Labour Management',
        experienceYears: 14,
        expectedWage: 1600,
        isAvailable: true,
        rating: 4.8,
        completedJobsCount: 74,
        city: 'Bengaluru',
      },
      {
        id: 3,
        name: 'Suresh Reddy (Reddy Infra)',
        phone: '+91 99002 44551',
        role: 'CONTRACTOR',
        language: 'en',
        status: 'VERIFIED',
        skills: 'General Contracting, Structural Works',
        experienceYears: 22,
        expectedWage: 0,
        isAvailable: true,
        rating: 4.9,
        completedJobsCount: 120,
        city: 'Bengaluru',
      },
      {
        id: 4,
        name: 'SiteMitra Admin',
        phone: '+91 80 4012 8899',
        role: 'ADMIN',
        language: 'en',
        status: 'VERIFIED',
        skills: 'Pilot Operations, Moderation & Safety',
        experienceYears: 10,
        expectedWage: 0,
        isAvailable: true,
        rating: 5.0,
        completedJobsCount: 500,
        city: 'Bengaluru',
      },
    ],
    sites: [
      {
        id: 1,
        contractorId: 3,
        supervisorId: 2,
        name: 'Nalini Towers Enclave',
        developer: 'Reddy Infra & Properties',
        address: 'Near ITPL, Whitefield, Bengaluru',
        area: 'Whitefield',
        latitude: 12.985,
        longitude: 77.7314,
        status: 'ACTIVE',
        totalWorkersNeeded: 50,
        activeWorkersHired: 42,
        estimatedCompletion: 'Dec 2026',
      },
      {
        id: 2,
        contractorId: 3,
        supervisorId: 2,
        name: 'Brigade Tech Park Extension',
        developer: 'Brigade Enterprises',
        address: 'Velankani Drive, Electronic City Phase 1',
        area: 'Electronic City',
        latitude: 12.8452,
        longitude: 77.6602,
        status: 'ACTIVE',
        totalWorkersNeeded: 65,
        activeWorkersHired: 54,
        estimatedCompletion: 'August 2027',
      },
      {
        id: 3,
        contractorId: 3,
        supervisorId: 2,
        name: 'Prestige Green Glen Apartments',
        developer: 'Prestige Estates',
        address: 'Kaveriappa Compound, Bellandur ORR',
        area: 'Bellandur',
        latitude: 12.9279,
        longitude: 77.6835,
        status: 'ACTIVE',
        totalWorkersNeeded: 40,
        activeWorkersHired: 35,
        estimatedCompletion: 'April 2027',
      },
      {
        id: 4,
        contractorId: 3,
        supervisorId: 2,
        name: 'Sobha Dream Acres Block 7',
        developer: 'Sobha Limited',
        address: 'Panathur Balagere Main Road, Varthur',
        area: 'Varthur',
        latitude: 12.934,
        longitude: 77.712,
        status: 'ACTIVE',
        totalWorkersNeeded: 55,
        activeWorkersHired: 48,
        estimatedCompletion: 'Nov 2027',
      },
      {
        id: 5,
        contractorId: 3,
        supervisorId: 2,
        name: 'Godrej Splendour Towers',
        developer: 'Godrej Properties',
        address: 'Belathur Road, Kadugodi, Bengaluru',
        area: 'Kadugodi',
        latitude: 12.9985,
        longitude: 77.7612,
        status: 'ACTIVE',
        totalWorkersNeeded: 35,
        activeWorkersHired: 26,
        estimatedCompletion: 'Feb 2027',
      },
      {
        id: 6,
        contractorId: 3,
        supervisorId: 2,
        name: 'Purva Marina Palms',
        developer: 'Puravankara Projects',
        address: 'Bellary Road, Hebbal Flyover Junction',
        area: 'Hebbal',
        latitude: 13.0358,
        longitude: 77.597,
        status: 'ACTIVE',
        totalWorkersNeeded: 45,
        activeWorkersHired: 39,
        estimatedCompletion: 'June 2027',
      },
    ],
    jobs: [
      {
        id: 1,
        siteId: 1,
        supervisorId: 2,
        title: 'Urgent: 8 Master Masons for Solid Brick Laying',
        category: 'Mason',
        wagePerDay: 950,
        vacancies: 8,
        hiredCount: 5,
        startDate: 'Tomorrow, 8:00 AM',
        workDurationDays: 25,
        foodProvided: true,
        stayProvided: true,
        isUrgent: true,
        siteName: 'Nalini Towers Enclave',
        siteAddress: 'Near ITPL, Whitefield, Bengaluru',
        area: 'Whitefield',
        supervisorName: 'Mestri Govindappa',
        supervisorPhone: '+91 98451 22340',
        distanceKm: 2.4,
        status: 'OPEN',
        postedAgo: '1 hour ago',
        isVerified: true,
      },
      {
        id: 2,
        siteId: 2,
        supervisorId: 2,
        title: 'Electricians for 3-Phase Conduit Wiring & DB Panels',
        category: 'Electrician',
        wagePerDay: 1100,
        vacancies: 6,
        hiredCount: 4,
        startDate: 'Immediate Joining',
        workDurationDays: 40,
        foodProvided: true,
        stayProvided: false,
        isUrgent: true,
        siteName: 'Brigade Tech Park Extension',
        siteAddress: 'Velankani Drive, Electronic City Phase 1',
        area: 'Electronic City',
        supervisorName: 'Mestri Manjunath',
        supervisorPhone: '+91 94480 33419',
        distanceKm: 5.8,
        status: 'OPEN',
        postedAgo: '3 hours ago',
        isVerified: true,
      },
      {
        id: 3,
        siteId: 3,
        supervisorId: 2,
        title: 'CPVC Plumbing Technicians & Helper Assistants',
        category: 'Plumber',
        wagePerDay: 900,
        vacancies: 5,
        hiredCount: 2,
        startDate: '12 Nov 2026',
        workDurationDays: 30,
        foodProvided: true,
        stayProvided: false,
        isUrgent: false,
        siteName: 'Prestige Green Glen Apartments',
        siteAddress: 'Kaveriappa Compound, Bellandur ORR',
        area: 'Bellandur',
        supervisorName: 'Mestri Govindappa',
        supervisorPhone: '+91 98451 22340',
        distanceKm: 3.9,
        status: 'OPEN',
        postedAgo: 'Yesterday',
        isVerified: true,
      },
      {
        id: 4,
        siteId: 4,
        supervisorId: 2,
        title: 'Bar Benders for 6th Floor Heavy Slab Casting',
        category: 'Bar Bender',
        wagePerDay: 1050,
        vacancies: 10,
        hiredCount: 6,
        startDate: 'Tomorrow Morning',
        workDurationDays: 15,
        foodProvided: true,
        stayProvided: true,
        isUrgent: true,
        siteName: 'Sobha Dream Acres Block 7',
        siteAddress: 'Panathur Balagere Main Road, Varthur',
        area: 'Varthur',
        supervisorName: 'Mestri Venkatesh',
        supervisorPhone: '+91 97312 88201',
        distanceKm: 4.2,
        status: 'OPEN',
        postedAgo: 'Just now',
        isVerified: true,
      },
      {
        id: 5,
        siteId: 5,
        supervisorId: 2,
        title: 'Exterior Weatherproof Paint & Wall Putty Finishers',
        category: 'Painter',
        wagePerDay: 850,
        vacancies: 6,
        hiredCount: 3,
        startDate: '14 Nov 2026',
        workDurationDays: 45,
        foodProvided: true,
        stayProvided: false,
        isUrgent: false,
        siteName: 'Godrej Splendour Towers',
        siteAddress: 'Belathur Road, Kadugodi, Bengaluru',
        area: 'Kadugodi',
        supervisorName: 'Mestri Govindappa',
        supervisorPhone: '+91 98451 22340',
        distanceKm: 6.1,
        status: 'OPEN',
        postedAgo: '4 hours ago',
        isVerified: true,
      },
      {
        id: 6,
        siteId: 6,
        supervisorId: 2,
        title: 'Aluminium Formwork & Shuttering Carpenters',
        category: 'Carpenter',
        wagePerDay: 1150,
        vacancies: 7,
        hiredCount: 5,
        startDate: 'Immediate',
        workDurationDays: 60,
        foodProvided: true,
        stayProvided: true,
        isUrgent: true,
        siteName: 'Purva Marina Palms',
        siteAddress: 'Bellary Road, Hebbal Flyover Junction',
        area: 'Hebbal',
        supervisorName: 'Mestri Shiva',
        supervisorPhone: '+91 96112 00934',
        distanceKm: 8.5,
        status: 'OPEN',
        postedAgo: 'Today',
        isVerified: true,
      },
      {
        id: 7,
        siteId: 1,
        supervisorId: 2,
        title: 'General Construction Helpers (Material Handling & Curing)',
        category: 'Helper',
        wagePerDay: 700,
        vacancies: 12,
        hiredCount: 8,
        startDate: 'Immediate',
        workDurationDays: 90,
        foodProvided: true,
        stayProvided: true,
        isUrgent: false,
        siteName: 'Nalini Towers Enclave',
        siteAddress: 'Near ITPL, Whitefield, Bengaluru',
        area: 'Whitefield',
        supervisorName: 'Mestri Govindappa',
        supervisorPhone: '+91 98451 22340',
        distanceKm: 2.4,
        status: 'OPEN',
        postedAgo: '5 hours ago',
        isVerified: true,
      },
      {
        id: 8,
        siteId: 3,
        supervisorId: 2,
        title: 'MS Railing & Safety Balcony Welders',
        category: 'Welder',
        wagePerDay: 1000,
        vacancies: 4,
        hiredCount: 2,
        startDate: '15 Nov 2026',
        workDurationDays: 20,
        foodProvided: true,
        stayProvided: false,
        isUrgent: false,
        siteName: 'Prestige Green Glen Apartments',
        siteAddress: 'Kaveriappa Compound, Bellandur ORR',
        area: 'Bellandur',
        supervisorName: 'Mestri Govindappa',
        supervisorPhone: '+91 98451 22340',
        distanceKm: 3.9,
        status: 'OPEN',
        postedAgo: 'Yesterday',
        isVerified: true,
      },
    ],
    applications: [
      {
        id: 1,
        jobId: 1,
        jobTitle: 'Urgent: 8 Master Masons for Solid Brick Laying',
        siteName: 'Nalini Towers Enclave',
        wagePerDay: 950,
        workerId: 1,
        workerName: 'Ramesh Kumar',
        workerPhone: '+91 98450 78210',
        workerSkill: 'Mason',
        workerExperience: 6,
        status: 'HIRED',
        appliedDate: 'Yesterday, 10:30 AM',
        supervisorPhone: '+91 98451 22340',
      },
      {
        id: 2,
        jobId: 2,
        jobTitle: 'Electricians for 3-Phase Conduit Wiring & DB Panels',
        siteName: 'Brigade Tech Park Extension',
        wagePerDay: 1100,
        workerId: 102,
        workerName: 'Anand Swamy (ಆನಂದ್)',
        workerPhone: '+91 94481 99012',
        workerSkill: 'Electrician',
        workerExperience: 5,
        status: 'SHORTLISTED',
        appliedDate: 'Today, 9:15 AM',
        supervisorPhone: '+91 94480 33419',
      },
      {
        id: 3,
        jobId: 4,
        jobTitle: 'Bar Benders for 6th Floor Heavy Slab Casting',
        siteName: 'Sobha Dream Acres Block 7',
        wagePerDay: 1050,
        workerId: 103,
        workerName: 'Mohan Lal (मोहन)',
        workerPhone: '+91 98230 45671',
        workerSkill: 'Bar Bender',
        workerExperience: 7,
        status: 'APPLIED',
        appliedDate: 'Today, 11:00 AM',
        supervisorPhone: '+91 97312 88201',
      },
      {
        id: 4,
        jobId: 6,
        jobTitle: 'Aluminium Formwork & Shuttering Carpenters',
        siteName: 'Purva Marina Palms',
        wagePerDay: 1150,
        workerId: 104,
        workerName: 'Basavaraju (ಬಸವರಾಜು)',
        workerPhone: '+91 97410 33812',
        workerSkill: 'Carpenter',
        workerExperience: 8,
        status: 'HIRED',
        appliedDate: '08 Nov 2026',
        supervisorPhone: '+91 96112 00934',
      },
      {
        id: 5,
        jobId: 1,
        jobTitle: 'Urgent: 8 Master Masons for Solid Brick Laying',
        siteName: 'Nalini Towers Enclave',
        wagePerDay: 950,
        workerId: 105,
        workerName: 'Chandrappa (ಚಂದ್ರಪ್ಪ)',
        workerPhone: '+91 98455 12890',
        workerSkill: 'Mason',
        workerExperience: 4,
        status: 'APPLIED',
        appliedDate: 'Today, 1:20 PM',
        supervisorPhone: '+91 98451 22340',
      },
    ],
    attendance: [
      {
        id: 1,
        siteId: 1,
        siteName: 'Nalini Towers Enclave',
        workerId: 1,
        workerName: 'Ramesh Kumar (Mason)',
        skill: 'Mason',
        date: '09 Nov 2026',
        status: 'PRESENT',
        dailyWage: 950,
        advancePaid: 0,
        notes: 'Completed block A north wall',
      },
      {
        id: 2,
        siteId: 1,
        siteName: 'Nalini Towers Enclave',
        workerId: 105,
        workerName: 'Chandrappa (Mason)',
        skill: 'Mason',
        date: '09 Nov 2026',
        status: 'PRESENT',
        dailyWage: 950,
        advancePaid: 200,
        notes: 'Advance given for bus fare',
      },
      {
        id: 3,
        siteId: 1,
        siteName: 'Nalini Towers Enclave',
        workerId: 108,
        workerName: 'Siddaramaiah (Helper)',
        skill: 'Helper',
        date: '09 Nov 2026',
        status: 'PRESENT',
        dailyWage: 700,
        advancePaid: 0,
        notes: 'Mortar mix duty',
      },
      {
        id: 4,
        siteId: 1,
        siteName: 'Nalini Towers Enclave',
        workerId: 109,
        workerName: 'Raju Naik (Helper)',
        skill: 'Helper',
        date: '09 Nov 2026',
        status: 'HALF_DAY',
        dailyWage: 700,
        advancePaid: 0,
        notes: 'Left at 1:30 PM due to fever',
      },
      {
        id: 5,
        siteId: 2,
        siteName: 'Brigade Tech Park Extension',
        workerId: 110,
        workerName: 'Kiran Gowda (Electrician)',
        skill: 'Electrician',
        date: '09 Nov 2026',
        status: 'PRESENT',
        dailyWage: 1100,
        advancePaid: 0,
        notes: 'Conduit floor 3 passed inspection',
      },
    ],
    reports: [
      {
        id: 1,
        category: 'Safety Equipment Missing',
        reportedBy: 'Worker Union Rep (Bengaluru)',
        againstEntity: 'Sub-contractor at Panathur Site',
        description: 'Shortage of safety harnesses on 7th floor scaffolding. Notified supervisor.',
        status: 'RESOLVED',
        date: '07 Nov 2026',
      },
      {
        id: 2,
        category: 'Wage Verification',
        reportedBy: 'Siddaraju (Mason)',
        againstEntity: 'Private Villa Project - Sarjapur',
        description: 'Daily wage offered was ₹800 vs advertised ₹950. Listing was flagged for moderation.',
        status: 'RESOLVED',
        date: '08 Nov 2026',
      },
      {
        id: 3,
        category: 'Drinking Water & First Aid',
        reportedBy: 'Site Safety Inspector',
        againstEntity: 'Electronic City Site B',
        description: 'Clean RO drinking water tank refilled and first aid kit restocked.',
        status: 'RESOLVED',
        date: '09 Nov 2026',
      },
    ],
  };
}

function createDbStore(dbFilePath = DB_FILE) {
  const dir = path.dirname(dbFilePath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }

  function load() {
    if (!fs.existsSync(dbFilePath)) {
      const seed = getSeedData();
      save(seed);
      return seed;
    }
    try {
      const raw = fs.readFileSync(dbFilePath, 'utf8');
      const parsed = JSON.parse(raw);
      return {
        users: Array.isArray(parsed.users) ? parsed.users : [],
        sites: Array.isArray(parsed.sites) ? parsed.sites : [],
        jobs: Array.isArray(parsed.jobs) ? parsed.jobs : [],
        applications: Array.isArray(parsed.applications) ? parsed.applications : [],
        attendance: Array.isArray(parsed.attendance) ? parsed.attendance : [],
        reports: Array.isArray(parsed.reports) ? parsed.reports : [],
      };
    } catch (e) {
      const seed = getSeedData();
      save(seed);
      return seed;
    }
  }

  function save(db) {
    const tmpFile = `${dbFilePath}.tmp`;
    fs.writeFileSync(tmpFile, JSON.stringify(db, null, 2), 'utf8');
    fs.renameSync(tmpFile, dbFilePath);
  }

  function nextId(collection) {
    if (!collection || collection.length === 0) return 1;
    return Math.max(...collection.map((item) => Number(item.id) || 0)) + 1;
  }

  function upsertById(collection, incoming) {
    if (!Array.isArray(incoming)) return collection;
    const map = new Map(collection.map((item) => [Number(item.id), item]));
    for (const item of incoming) {
      const itemId = Number(item.id);
      if (itemId > 0) {
        map.set(itemId, { ...map.get(itemId), ...item, id: itemId });
      } else {
        const assignedId = nextId(Array.from(map.values()));
        map.set(assignedId, { ...item, id: assignedId });
      }
    }
    return Array.from(map.values());
  }

  // Initialize on boot
  load();

  return { load, save, nextId, upsertById };
}

function sendJson(res, statusCode, payload) {
  res.writeHead(statusCode, {
    'Content-Type': 'application/json; charset=utf-8',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE, OPTIONS',
    'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With',
    'Cache-Control': 'no-store',
  });
  res.end(JSON.stringify(payload));
}

function parseBody(req) {
  return new Promise((resolve, reject) => {
    let raw = '';
    req.on('data', (chunk) => {
      raw += chunk;
      if (raw.length > 5 * 1024 * 1024) {
        reject(new Error('Payload too large'));
      }
    });
    req.on('end', () => {
      if (!raw || raw.trim() === '') return resolve({});
      try {
        resolve(JSON.parse(raw));
      } catch (err) {
        reject(new Error('Invalid JSON body'));
      }
    });
    req.on('error', reject);
  });
}

function createApp(dbFilePath = DB_FILE) {
  const store = createDbStore(dbFilePath);

  return http.createServer(async (req, res) => {
    // Handle CORS preflight
    if (req.method === 'OPTIONS') {
      res.writeHead(204, {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE, OPTIONS',
        'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With',
      });
      return res.end();
    }

    const parsedUrl = new URL(req.url, 'http://localhost');
    const pathname = parsedUrl.pathname;

    // --- API DATA SERVER ROUTES ---
    if (pathname.startsWith('/api/')) {
      try {
        // 1. Health & Telemetry
        if (pathname === '/api/health' && (req.method === 'GET' || req.method === 'HEAD')) {
          const db = store.load();
          if (req.method === 'HEAD') {
            res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
            return res.end();
          }
          return sendJson(res, 200, {
            status: 'ok',
            service: 'SiteMitra Data Server',
            region: 'Google Cloud Run (asia-northeast1)',
            version: '1.0.0',
            counts: {
              users: db.users.length,
              sites: db.sites.length,
              jobs: db.jobs.length,
              applications: db.applications.length,
              attendance: db.attendance.length,
              reports: db.reports.length,
            },
            timestamp: new Date().toISOString(),
          });
        }

        // 2. Full Sync Endpoint
        if (pathname === '/api/sync') {
          if (req.method === 'GET') {
            const db = store.load();
            return sendJson(res, 200, {
              ...db,
              syncedAt: new Date().toISOString(),
            });
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            const db = store.load();
            if (body.users) db.users = store.upsertById(db.users, body.users);
            if (body.sites) db.sites = store.upsertById(db.sites, body.sites);
            if (body.jobs) db.jobs = store.upsertById(db.jobs, body.jobs);
            if (body.applications) db.applications = store.upsertById(db.applications, body.applications);
            if (body.attendance) db.attendance = store.upsertById(db.attendance, body.attendance);
            if (body.reports) db.reports = store.upsertById(db.reports, body.reports);
            store.save(db);
            return sendJson(res, 200, {
              ...db,
              syncedAt: new Date().toISOString(),
            });
          }
        }

        // 3. Users
        if (pathname === '/api/users') {
          const db = store.load();
          if (req.method === 'GET') {
            const phone = parsedUrl.searchParams.get('phone');
            const role = parsedUrl.searchParams.get('role');
            let users = db.users;
            if (phone) {
              const digits = phone.replace(/\D/g, '').slice(-10);
              users = users.filter((u) => String(u.phone).replace(/\D/g, '').endsWith(digits));
            }
            if (role) {
              users = users.filter((u) => String(u.role).toUpperCase() === role.toUpperCase());
            }
            return sendJson(res, 200, users);
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            if (!body.name || !body.phone) {
              return sendJson(res, 400, { error: 'name and phone are required' });
            }
            const digits = String(body.phone).replace(/\D/g, '').slice(-10);
            const existingIdx = db.users.findIndex(
              (u) => (body.id && Number(u.id) === Number(body.id)) ||
                     (digits.length === 10 && String(u.phone).replace(/\D/g, '').endsWith(digits))
            );
            let savedUser;
            if (existingIdx >= 0) {
              savedUser = { ...db.users[existingIdx], ...body, id: db.users[existingIdx].id };
              db.users[existingIdx] = savedUser;
            } else {
              savedUser = {
                role: 'WORKER',
                language: 'en',
                status: 'VERIFIED',
                skills: 'Mason, Brickwork',
                experienceYears: 3,
                expectedWage: 900,
                isAvailable: true,
                rating: 4.8,
                completedJobsCount: 0,
                city: 'Bengaluru',
                ...body,
                id: store.nextId(db.users),
              };
              db.users.push(savedUser);
            }
            store.save(db);
            return sendJson(res, 201, savedUser);
          }
        }

        // 4. Construction Sites
        if (pathname === '/api/sites') {
          const db = store.load();
          if (req.method === 'GET') {
            return sendJson(res, 200, db.sites);
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            if (!body.name || !body.area) {
              return sendJson(res, 400, { error: 'name and area are required' });
            }
            const site = {
              contractorId: 3,
              supervisorId: 2,
              developer: 'SiteMitra Partner',
              address: `${body.area}, Bengaluru`,
              latitude: 12.9716,
              longitude: 77.5946,
              status: 'ACTIVE',
              totalWorkersNeeded: 30,
              activeWorkersHired: 0,
              estimatedCompletion: 'Dec 2027',
              ...body,
              id: body.id && Number(body.id) > 0 ? Number(body.id) : store.nextId(db.sites),
            };
            const idx = db.sites.findIndex((s) => Number(s.id) === site.id);
            if (idx >= 0) db.sites[idx] = site;
            else db.sites.push(site);
            store.save(db);
            return sendJson(res, 201, site);
          }
        }

        // 5. Jobs
        if (pathname === '/api/jobs') {
          const db = store.load();
          if (req.method === 'GET') {
            const category = parsedUrl.searchParams.get('category');
            const area = parsedUrl.searchParams.get('area');
            const search = parsedUrl.searchParams.get('search');
            const urgent = parsedUrl.searchParams.get('urgent');
            let jobs = [...db.jobs].sort((a, b) => (b.isUrgent ? 1 : 0) - (a.isUrgent ? 1 : 0) || b.id - a.id);
            if (category && category !== 'all') {
              jobs = jobs.filter((j) => String(j.category).toLowerCase() === category.toLowerCase());
            }
            if (area && area !== 'all') {
              jobs = jobs.filter((j) => String(j.area).toLowerCase() === area.toLowerCase());
            }
            if (urgent === 'true') {
              jobs = jobs.filter((j) => Boolean(j.isUrgent));
            }
            if (search) {
              const q = search.toLowerCase();
              jobs = jobs.filter(
                (j) =>
                  String(j.title).toLowerCase().includes(q) ||
                  String(j.siteName).toLowerCase().includes(q) ||
                  String(j.area).toLowerCase().includes(q) ||
                  String(j.category).toLowerCase().includes(q)
              );
            }
            return sendJson(res, 200, jobs);
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            if (!body.title || !body.siteName) {
              return sendJson(res, 400, { error: 'title and siteName are required' });
            }
            const job = {
              siteId: 1,
              supervisorId: 2,
              category: 'Mason',
              wagePerDay: 900,
              vacancies: 5,
              hiredCount: 0,
              startDate: 'Tomorrow, 8:00 AM',
              workDurationDays: 30,
              foodProvided: true,
              stayProvided: false,
              isUrgent: false,
              siteAddress: `${body.area || 'Bengaluru'}, Karnataka`,
              area: 'Whitefield',
              supervisorName: 'Mestri Govindappa',
              supervisorPhone: '+91 98451 22340',
              distanceKm: 3.0,
              status: 'OPEN',
              postedAgo: 'Just now',
              isVerified: true,
              ...body,
              id: body.id && Number(body.id) > 0 ? Number(body.id) : store.nextId(db.jobs),
            };
            const idx = db.jobs.findIndex((j) => Number(j.id) === job.id);
            if (idx >= 0) db.jobs[idx] = job;
            else db.jobs.unshift(job);
            store.save(db);
            return sendJson(res, 201, job);
          }
        }

        const jobIdMatch = pathname.match(/^\/api\/jobs\/(\d+)$/);
        if (jobIdMatch && (req.method === 'PATCH' || req.method === 'PUT')) {
          const jobId = Number(jobIdMatch[1]);
          const body = await parseBody(req);
          const db = store.load();
          const idx = db.jobs.findIndex((j) => Number(j.id) === jobId);
          if (idx < 0) return sendJson(res, 404, { error: 'Job not found' });
          db.jobs[idx] = { ...db.jobs[idx], ...body, id: jobId };
          store.save(db);
          return sendJson(res, 200, db.jobs[idx]);
        }

        // 6. Applications
        if (pathname === '/api/applications') {
          const db = store.load();
          if (req.method === 'GET') {
            const workerId = parsedUrl.searchParams.get('workerId');
            const jobId = parsedUrl.searchParams.get('jobId');
            let apps = [...db.applications].sort((a, b) => b.id - a.id);
            if (workerId) apps = apps.filter((a) => Number(a.workerId) === Number(workerId));
            if (jobId) apps = apps.filter((a) => Number(a.jobId) === Number(jobId));
            return sendJson(res, 200, apps);
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            if (!body.jobId || !body.workerName) {
              return sendJson(res, 400, { error: 'jobId and workerName are required' });
            }
            const application = {
              jobTitle: 'Construction Role',
              siteName: 'Bengaluru Site',
              wagePerDay: 900,
              workerId: 1,
              workerPhone: '+91 98450 78210',
              workerSkill: 'Mason',
              workerExperience: 3,
              status: 'APPLIED',
              appliedDate: 'Today, just now',
              supervisorPhone: '+91 98451 22340',
              ...body,
              id: body.id && Number(body.id) > 0 ? Number(body.id) : store.nextId(db.applications),
            };
            const idx = db.applications.findIndex((a) => Number(a.id) === application.id);
            if (idx >= 0) db.applications[idx] = application;
            else db.applications.unshift(application);
            store.save(db);
            return sendJson(res, 201, application);
          }
        }

        const appIdMatch = pathname.match(/^\/api\/applications\/(\d+)$/);
        if (appIdMatch && (req.method === 'PATCH' || req.method === 'PUT')) {
          const appId = Number(appIdMatch[1]);
          const body = await parseBody(req);
          const db = store.load();
          const idx = db.applications.findIndex((a) => Number(a.id) === appId);
          if (idx < 0) return sendJson(res, 404, { error: 'Application not found' });
          const prevStatus = db.applications[idx].status;
          db.applications[idx] = { ...db.applications[idx], ...body, id: appId };

          if (body.status === 'HIRED' && prevStatus !== 'HIRED') {
            const jobIdx = db.jobs.findIndex((j) => Number(j.id) === Number(db.applications[idx].jobId));
            if (jobIdx >= 0) {
              db.jobs[jobIdx].hiredCount = (Number(db.jobs[jobIdx].hiredCount) || 0) + 1;
            }
          }
          store.save(db);
          return sendJson(res, 200, db.applications[idx]);
        }

        // 7. Attendance
        if (pathname === '/api/attendance') {
          const db = store.load();
          if (req.method === 'GET') {
            const siteId = parsedUrl.searchParams.get('siteId');
            let list = [...db.attendance].sort((a, b) => b.id - a.id);
            if (siteId) list = list.filter((a) => Number(a.siteId) === Number(siteId));
            return sendJson(res, 200, list);
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            if (!body.workerName) {
              return sendJson(res, 400, { error: 'workerName is required' });
            }
            const record = {
              siteId: 1,
              siteName: 'Nalini Towers Enclave',
              workerId: 1,
              skill: 'Mason',
              date: new Date().toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }),
              status: 'PRESENT',
              dailyWage: 900,
              advancePaid: 0,
              notes: '',
              ...body,
              id: body.id && Number(body.id) > 0 ? Number(body.id) : store.nextId(db.attendance),
            };
            const idx = db.attendance.findIndex((a) => Number(a.id) === record.id);
            if (idx >= 0) db.attendance[idx] = record;
            else db.attendance.unshift(record);
            store.save(db);
            return sendJson(res, 201, record);
          }
        }

        const attIdMatch = pathname.match(/^\/api\/attendance\/(\d+)$/);
        if (attIdMatch && (req.method === 'PATCH' || req.method === 'PUT')) {
          const attId = Number(attIdMatch[1]);
          const body = await parseBody(req);
          const db = store.load();
          const idx = db.attendance.findIndex((a) => Number(a.id) === attId);
          if (idx < 0) return sendJson(res, 404, { error: 'Attendance record not found' });
          db.attendance[idx] = { ...db.attendance[idx], ...body, id: attId };
          store.save(db);
          return sendJson(res, 200, db.attendance[idx]);
        }

        // 8. Platform Reports & Grievances
        if (pathname === '/api/reports') {
          const db = store.load();
          if (req.method === 'GET') {
            return sendJson(res, 200, [...db.reports].sort((a, b) => b.id - a.id));
          }
          if (req.method === 'POST') {
            const body = await parseBody(req);
            if (!body.category || !body.description) {
              return sendJson(res, 400, { error: 'category and description are required' });
            }
            const report = {
              reportedBy: 'Worker',
              againstEntity: 'Construction Site',
              status: 'INVESTIGATING',
              date: 'Nov 2026',
              ...body,
              id: body.id && Number(body.id) > 0 ? Number(body.id) : store.nextId(db.reports),
            };
            db.reports.unshift(report);
            store.save(db);
            return sendJson(res, 201, report);
          }
        }

        const reportIdMatch = pathname.match(/^\/api\/reports\/(\d+)$/);
        if (reportIdMatch && (req.method === 'PATCH' || req.method === 'PUT')) {
          const reportId = Number(reportIdMatch[1]);
          const body = await parseBody(req);
          const db = store.load();
          const idx = db.reports.findIndex((r) => Number(r.id) === reportId);
          if (idx < 0) return sendJson(res, 404, { error: 'Report not found' });
          db.reports[idx] = { ...db.reports[idx], ...body, id: reportId };
          store.save(db);
          return sendJson(res, 200, db.reports[idx]);
        }

        return sendJson(res, 404, { error: `API route not found: ${pathname}` });
      } catch (err) {
        return sendJson(res, 400, { error: err.message || 'Bad Request' });
      }
    }

    // --- STATIC & APK ROUTES ---
    let reqPath = pathname;
    if (reqPath === '/' || reqPath === '') {
      reqPath = '/index.html';
    }

    // Direct APK download route for friends
    if (reqPath === '/download/app.apk' || reqPath === '/app-debug.apk' || reqPath === '/SiteMitra.apk') {
      const apkPath = path.join(ROOT, 'app/build/outputs/apk/debug/app-debug.apk');
      if (fs.existsSync(apkPath)) {
        res.writeHead(200, {
          'Content-Type': 'application/vnd.android.package-archive',
          'Content-Disposition': 'attachment; filename="SiteMitra.apk"',
        });
        return fs.createReadStream(apkPath).pipe(res);
      }
    }

    const safeFilePath = path.normalize(path.join(ROOT, reqPath));
    if (safeFilePath.startsWith(ROOT) && fs.existsSync(safeFilePath) && fs.statSync(safeFilePath).isFile()) {
      const ext = path.extname(safeFilePath);
      res.writeHead(200, { 'Content-Type': MIME_TYPES[ext] || 'application/octet-stream' });
      return fs.createReadStream(safeFilePath).pipe(res);
    }

    // Fallback to index.html for Single-Page Application routing
    const indexPath = path.join(ROOT, 'index.html');
    if (fs.existsSync(indexPath)) {
      res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
      return fs.createReadStream(indexPath).pipe(res);
    }

    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Not Found');
  });
}

if (require.main === module) {
  const server = createApp();
  server.listen(PORT, '0.0.0.0', () => {
    console.log(`SiteMitra Data Server running at http://0.0.0.0:${PORT}`);
  });
}

module.exports = { createApp, createDbStore, getSeedData };
