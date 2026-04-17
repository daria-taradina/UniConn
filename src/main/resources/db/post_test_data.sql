-- ============================================================
-- post_test_data.sql
-- Run AFTER: 1) users_test_data.sql  2) community_test_data.sql
--
-- 40 Posts total:
--   Part 1 — 20 profile posts (community_id = NULL, any user)
--   Part 2 — 20 community posts (author must be a member)
--
-- Post tags reference existing tags from community_test_data.sql
-- ============================================================
USE uni_conn;

-- ============================================================
-- Part 1: Profile Posts (no community)
-- ============================================================
INSERT INTO post (author_id, community_id, title, content_text, like_count, comment_count, created_at, is_deleted)
VALUES

-- alex_m92
((SELECT user_id FROM users WHERE username = 'alex_m92'),
 NULL,
 'Finally pushed my capstone to GitHub',
 'After three weeks of refactoring, the UniConn backend is live on GitHub. Spring Boot + MySQL, still polishing the auth flow. #springboot #java',
 12, 3, NOW() - INTERVAL 10 DAY, false),

-- brianna_t
((SELECT user_id FROM users WHERE username = 'brianna_t'),
 NULL,
 'Pandas vs Polars — which one do you actually use?',
 'Been switching between Pandas and Polars for my data pipeline project. Polars is insanely fast but the API takes getting used to. Anyone else made the switch? #python #datavisualization',
 9, 5, NOW() - INTERVAL 9 DAY, false),

-- carlos_v
((SELECT user_id FROM users WHERE username = 'carlos_v'),
 NULL,
 'React state management in 2025',
 'Zustand is winning me over. Used Redux for years but the boilerplate is exhausting. Zustand feels way more natural for mid-sized apps. #react #fullstack',
 14, 6, NOW() - INTERVAL 8 DAY, false),

-- diana_wu
((SELECT user_id FROM users WHERE username = 'diana_wu'),
 NULL,
 'Our robot arm finally works',
 'Six months of debugging and we finally got the inverse kinematics right. Demo day is next week — wish us luck! #robotics #engineering',
 22, 8, NOW() - INTERVAL 8 DAY, false),

-- ethan_rc
((SELECT user_id FROM users WHERE username = 'ethan_rc'),
 NULL,
 'Lessons from building an EdTech MVP',
 'We launched our tutoring platform beta to 30 students. Biggest lesson: teachers, not students, are your actual customers. #edtech #webdev',
 17, 7, NOW() - INTERVAL 7 DAY, false),

-- fatima_ok
((SELECT user_id FROM users WHERE username = 'fatima_ok'),
 NULL,
 'Dark mode is not just a preference — it''s an accessibility issue',
 'Did a UX audit last week and realized half our users rely on dark mode for eye strain reasons, not aesthetics. Design systems need to treat it as a requirement. #ux',
 19, 9, NOW() - INTERVAL 7 DAY, false),

-- george_lp
((SELECT user_id FROM users WHERE username = 'george_lp'),
 NULL,
 'How I approach CTF recon phase',
 'First 20 minutes of any CTF: nmap, directory bruteforce, check headers. Sounds basic but most teams skip it and waste hours. #cybersecurity #ctf',
 11, 4, NOW() - INTERVAL 6 DAY, false),

-- hannah_s9
((SELECT user_id FROM users WHERE username = 'hannah_s9'),
 NULL,
 'MCAT prep — 6 months out',
 'Started my MCAT schedule. Two hours every morning before class. Biochem is brutal. Anyone have good resources for C/P section? #mcat #premed',
 8, 6, NOW() - INTERVAL 6 DAY, false),

-- ivan_mk
((SELECT user_id FROM users WHERE username = 'ivan_mk'),
 NULL,
 'Sponsor pitch for Formula SAE went well',
 'Pitched to a local engineering firm today. They''re interested in sponsoring our brake system. Big deal for us — updates soon. #formulasae #engineering',
 15, 3, NOW() - INTERVAL 5 DAY, false),

-- jasmine_hl
((SELECT user_id FROM users WHERE username = 'jasmine_hl'),
 NULL,
 'AI-generated music is getting good — and weird',
 'Tried Suno and Udio for a project this week. The outputs are genuinely impressive but they hallucinate chord progressions in strange ways. #music #artificialintelligence',
 20, 11, NOW() - INTERVAL 5 DAY, false),

-- kevin_nb
((SELECT user_id FROM users WHERE username = 'kevin_nb'),
 NULL,
 'Indexing saved my query from 4s to 40ms',
 'Added a composite index on (user_id, created_at) and my feed query went from 4 seconds to 40ms. Always check your EXPLAIN output. #databases #java',
 25, 10, NOW() - INTERVAL 4 DAY, false),

-- laura_fd
((SELECT user_id FROM users WHERE username = 'laura_fd'),
 NULL,
 'Accessibility checklist I use on every project',
 'Sharing my personal a11y checklist: ARIA labels, keyboard nav, color contrast ratio, focus indicators, skip links. Feel free to steal it. #ux #webdev',
 18, 7, NOW() - INTERVAL 4 DAY, false),

-- miguel_cr
((SELECT user_id FROM users WHERE username = 'miguel_cr'),
 NULL,
 'Deployed my first Spring Boot app to Render',
 'Free tier works surprisingly well for a portfolio project. Cold start is slow but acceptable. Full guide coming soon. #springboot #fullstack',
 13, 5, NOW() - INTERVAL 3 DAY, false),

-- natalie_bw
((SELECT user_id FROM users WHERE username = 'natalie_bw'),
 NULL,
 'Why I switched from Figma to FigJam for early wireframes',
 'FigJam is so much faster for low-fidelity brainstorming. Save Figma for when you know what you''re building. #ux #edtech',
 10, 4, NOW() - INTERVAL 3 DAY, false),

-- omar_hs
((SELECT user_id FROM users WHERE username = 'omar_hs'),
 NULL,
 'Reverse engineering a login form for practice',
 'Set up a local vulnerable app and walked through SQL injection and XSS. Highly recommend DVWA for anyone learning web sec. #cybersecurity #ctf',
 16, 6, NOW() - INTERVAL 2 DAY, false),

-- paula_gm
((SELECT user_id FROM users WHERE username = 'paula_gm'),
 NULL,
 'Research shadowing experience — week 1',
 'Started shadowing at a clinic this week. Observing patient intake and vitals. It''s humbling how much you don''t know until you''re in the room. #premed',
 7, 3, NOW() - INTERVAL 2 DAY, false),

-- quinn_aj
((SELECT user_id FROM users WHERE username = 'quinn_aj'),
 NULL,
 'Building a quiz app for my EdTech portfolio',
 'Finished the first version of my adaptive quiz platform. Built with React + a Flask backend. Looking for beta testers! #edtech #react',
 11, 5, NOW() - INTERVAL 1 DAY, false),

-- raj_pt
((SELECT user_id FROM users WHERE username = 'raj_pt'),
 NULL,
 'Scikit-learn vs PyTorch for a classification task',
 'For tabular data with clean features, sklearn still wins. PyTorch is overkill unless you need custom architectures. #machinelearning #python',
 14, 7, NOW() - INTERVAL 1 DAY, false),

-- sofia_er
((SELECT user_id FROM users WHERE username = 'sofia_er'),
 NULL,
 'How music theory helps me think about ML model structure',
 'Tension and resolution in music maps surprisingly well to loss functions and convergence. Both are about finding balance. #music #machinelearning',
 21, 9, NOW() - INTERVAL 12 HOUR, false),

-- tyler_mc
((SELECT user_id FROM users WHERE username = 'tyler_mc'),
 NULL,
 'Git workflow for a 3-person backend team',
 'We use feature branches, squash merges, and a shared dev branch. No one pushes to main directly. Saved us from two disasters already. #java #fullstack',
 16, 6, NOW() - INTERVAL 6 HOUR, false);


-- ============================================================
-- Part 2: Community Posts (author must be a member)
-- ============================================================
INSERT INTO post (author_id, community_id, title, content_text, like_count, comment_count, created_at, is_deleted)
VALUES

-- CS_Capstone — members: alex_m92, carlos_v, kevin_nb, miguel_cr, raj_pt, tyler_mc
((SELECT user_id FROM users WHERE username = 'alex_m92'),
 (SELECT community_id FROM community WHERE community_name = 'CS_Capstone'),
 'Capstone milestone 2 checklist',
 'Posting our M2 checklist so we''re all on the same page: ER diagram finalized, REST endpoints documented, auth working. Let me know if I missed anything. #java #databases',
 8, 4, NOW() - INTERVAL 9 DAY, false),

((SELECT user_id FROM users WHERE username = 'kevin_nb'),
 (SELECT community_id FROM community WHERE community_name = 'CS_Capstone'),
 'Should we use JWT or session-based auth?',
 'I''m leaning JWT since we''re building a REST API, but open to discussion. Main concern is token expiry handling. #java #springboot',
 10, 7, NOW() - INTERVAL 7 DAY, false),

-- DataScience_Hub — members: brianna_t, jasmine_hl, kevin_nb, raj_pt, sofia_er
((SELECT user_id FROM users WHERE username = 'brianna_t'),
 (SELECT community_id FROM community WHERE community_name = 'DataScience_Hub'),
 'Free datasets for your ML projects',
 'Compiled a list of clean, well-documented public datasets: UCI repo, Kaggle open datasets, government open data portals. Great for portfolio projects. #python #machinelearning',
 15, 6, NOW() - INTERVAL 8 DAY, false),

((SELECT user_id FROM users WHERE username = 'raj_pt'),
 (SELECT community_id FROM community WHERE community_name = 'DataScience_Hub'),
 'Overfitting — how do you actually catch it early?',
 'Beyond train/val loss curves — what signals do you look for? I''ve been using learning curves and cross-validation but curious what others do. #machinelearning #datavisualization',
 12, 8, NOW() - INTERVAL 5 DAY, false),

-- FullStack_Dev — members: carlos_v, alex_m92, ethan_rc, kevin_nb, laura_fd, miguel_cr, tyler_mc
((SELECT user_id FROM users WHERE username = 'carlos_v'),
 (SELECT community_id FROM community WHERE community_name = 'FullStack_Dev'),
 'Spring Boot + React setup guide',
 'Wrote a step-by-step guide for our stack: Spring Boot handles REST, React consumes it via fetch with CORS configured. JWT for auth. Will paste repo link. #springboot #react #fullstack',
 18, 9, NOW() - INTERVAL 7 DAY, false),

((SELECT user_id FROM users WHERE username = 'laura_fd'),
 (SELECT community_id FROM community WHERE community_name = 'FullStack_Dev'),
 'CSS Modules vs Tailwind — which do you prefer?',
 'I''ve been using CSS Modules for scoped styles but Tailwind is tempting for speed. What''s your team using? #webdev #react',
 11, 10, NOW() - INTERVAL 4 DAY, false),

-- Robotics_Club — members: diana_wu, ivan_mk, miguel_cr, omar_hs, tyler_mc
((SELECT user_id FROM users WHERE username = 'diana_wu'),
 (SELECT community_id FROM community WHERE community_name = 'Robotics_Club'),
 'Demo day prep — what to bring',
 'Reminder: bring the full chassis, laptop with ROS installed, and backup battery pack. We present at 2pm in Engineering building. #robotics #engineering',
 9, 3, NOW() - INTERVAL 3 DAY, false),

((SELECT user_id FROM users WHERE username = 'ivan_mk'),
 (SELECT community_id FROM community WHERE community_name = 'Robotics_Club'),
 'Sensor fusion approach for obstacle detection',
 'We combined ultrasonic and IR sensors using a weighted average. Works well in controlled environments, less reliable outdoors. #robotics #engineering',
 13, 5, NOW() - INTERVAL 6 DAY, false),

-- EdTech_Builders — members: ethan_rc, laura_fd, natalie_bw, quinn_aj
((SELECT user_id FROM users WHERE username = 'ethan_rc'),
 (SELECT community_id FROM community WHERE community_name = 'EdTech_Builders'),
 'User research findings — student attention span',
 'Ran a quick study with 15 students. Optimal lesson segment length: 7-12 minutes. Anything longer and engagement drops sharply. #edtech #webdev',
 14, 6, NOW() - INTERVAL 5 DAY, false),

((SELECT user_id FROM users WHERE username = 'quinn_aj'),
 (SELECT community_id FROM community WHERE community_name = 'EdTech_Builders'),
 'Gamification ideas for our quiz platform',
 'Thinking about streaks, XP points, and leaderboards. But research shows leaderboards can demotivate lower-ranked users. Badge systems seem safer. #edtech #react',
 10, 7, NOW() - INTERVAL 2 DAY, false),

-- UX_Design_CSUN — members: fatima_ok, laura_fd, natalie_bw, paula_gm, quinn_aj
((SELECT user_id FROM users WHERE username = 'fatima_ok'),
 (SELECT community_id FROM community WHERE community_name = 'UX_Design_CSUN'),
 'Portfolio review — feedback welcome',
 'Just updated my UX portfolio with three new case studies. Looking for honest feedback especially on how I present my process. Drop a comment! #ux',
 16, 11, NOW() - INTERVAL 4 DAY, false),

((SELECT user_id FROM users WHERE username = 'natalie_bw'),
 (SELECT community_id FROM community WHERE community_name = 'UX_Design_CSUN'),
 'Figma prototyping tips I wish I knew earlier',
 'Auto layout is a game changer. Also: component variants save so much time. And please use a 8pt grid system — your developers will thank you. #ux #webdev',
 19, 8, NOW() - INTERVAL 2 DAY, false),

-- CyberSec_CTF — members: george_lp, alex_m92, kevin_nb, omar_hs, raj_pt
((SELECT user_id FROM users WHERE username = 'george_lp'),
 (SELECT community_id FROM community WHERE community_name = 'CyberSec_CTF'),
 'CTF team formation for PicoCTF',
 'PicoCTF starts next month. Who''s in? We need at least one person strong in forensics and one in crypto. Reply with your strongest category. #ctf #cybersecurity',
 17, 12, NOW() - INTERVAL 6 DAY, false),

((SELECT user_id FROM users WHERE username = 'omar_hs'),
 (SELECT community_id FROM community WHERE community_name = 'CyberSec_CTF'),
 'Write-up: last week''s binary exploitation challenge',
 'Walked through the buffer overflow challenge from last week''s practice set. Key insight was finding the offset using pattern_create. #ctf #cybersecurity',
 14, 5, NOW() - INTERVAL 3 DAY, false),

-- PreMed_Network — members: hannah_s9, natalie_bw, paula_gm, sofia_er
((SELECT user_id FROM users WHERE username = 'hannah_s9'),
 (SELECT community_id FROM community WHERE community_name = 'PreMed_Network'),
 'MCAT study group — anyone interested?',
 'Thinking of organizing a weekly MCAT study group, Sundays 10am at the library. Focused on C/P and B/B sections. Drop a comment if you''re in! #mcat #premed',
 11, 8, NOW() - INTERVAL 5 DAY, false),

((SELECT user_id FROM users WHERE username = 'sofia_er'),
 (SELECT community_id FROM community WHERE community_name = 'PreMed_Network'),
 'Research opportunity — neuroscience lab looking for volunteers',
 'Professor Kim''s lab in the biology department is looking for pre-med volunteers for a cognitive study. No experience required, good for your app. DM me. #premed',
 13, 6, NOW() - INTERVAL 2 DAY, false),

-- Formula_SAE — members: ivan_mk, diana_wu, miguel_cr, omar_hs
((SELECT user_id FROM users WHERE username = 'ivan_mk'),
 (SELECT community_id FROM community WHERE community_name = 'Formula_SAE'),
 'Suspension geometry update — new caster angle',
 'We adjusted the caster angle from 4° to 6° based on simulation feedback. Expect better straight-line stability. Testing this weekend. #formulasae #engineering',
 10, 4, NOW() - INTERVAL 4 DAY, false),

((SELECT user_id FROM users WHERE username = 'miguel_cr'),
 (SELECT community_id FROM community WHERE community_name = 'Formula_SAE'),
 'CAD files updated on shared drive',
 'Pushed the latest chassis CAD revisions. Major change: reinforced roll hoop mounting points. Review before Saturday''s build session. #formulasae #engineering',
 8, 3, NOW() - INTERVAL 1 DAY, false),

-- Music_And_AI — members: jasmine_hl, brianna_t, laura_fd, quinn_aj, sofia_er
((SELECT user_id FROM users WHERE username = 'jasmine_hl'),
 (SELECT community_id FROM community WHERE community_name = 'Music_And_AI'),
 'Paper share: music generation with transformers',
 'Found a great paper on using transformer architectures for symbolic music generation. Link in comments. The attention maps on chord progressions are fascinating. #artificialintelligence #machinelearning',
 20, 9, NOW() - INTERVAL 3 DAY, false),

((SELECT user_id FROM users WHERE username = 'sofia_er'),
 (SELECT community_id FROM community WHERE community_name = 'Music_And_AI'),
 'Anyone tried fine-tuning a model on their own compositions?',
 'I fine-tuned a small GPT-2 variant on 50 MIDI transcriptions of my piano pieces. Results are surreal — recognizably mine but unpredictably different. #artificialintelligence #music',
 23, 13, NOW() - INTERVAL 1 DAY, false);


-- ============================================================
-- Post Tags
-- Tags reference existing tag rows from community_test_data.sql
-- Only tags relevant to each post's content are applied
-- ============================================================
INSERT INTO post_tag (post_id, tag_id)
VALUES
-- Post 1: alex_m92 profile — #springboot #java
((SELECT post_id FROM post WHERE title = 'Finally pushed my capstone to GitHub'),        (SELECT tag_id FROM tag WHERE name = 'springboot')),
((SELECT post_id FROM post WHERE title = 'Finally pushed my capstone to GitHub'),        (SELECT tag_id FROM tag WHERE name = 'java')),

-- Post 2: brianna_t profile — #python #datavisualization
((SELECT post_id FROM post WHERE title = 'Pandas vs Polars — which one do you actually use?'), (SELECT tag_id FROM tag WHERE name = 'python')),
((SELECT post_id FROM post WHERE title = 'Pandas vs Polars — which one do you actually use?'), (SELECT tag_id FROM tag WHERE name = 'datavisualization')),

-- Post 3: carlos_v profile — #react #fullstack
((SELECT post_id FROM post WHERE title = 'React state management in 2025'),              (SELECT tag_id FROM tag WHERE name = 'react')),
((SELECT post_id FROM post WHERE title = 'React state management in 2025'),              (SELECT tag_id FROM tag WHERE name = 'fullstack')),

-- Post 4: diana_wu profile — #robotics #engineering
((SELECT post_id FROM post WHERE title = 'Our robot arm finally works'),                 (SELECT tag_id FROM tag WHERE name = 'robotics')),
((SELECT post_id FROM post WHERE title = 'Our robot arm finally works'),                 (SELECT tag_id FROM tag WHERE name = 'engineering')),

-- Post 5: ethan_rc profile — #edtech #webdev
((SELECT post_id FROM post WHERE title = 'Lessons from building an EdTech MVP'),        (SELECT tag_id FROM tag WHERE name = 'edtech')),
((SELECT post_id FROM post WHERE title = 'Lessons from building an EdTech MVP'),        (SELECT tag_id FROM tag WHERE name = 'webdev')),

-- Post 6: fatima_ok profile — #ux
((SELECT post_id FROM post WHERE title = 'Dark mode is not just a preference — it''s an accessibility issue'), (SELECT tag_id FROM tag WHERE name = 'ux')),

-- Post 7: george_lp profile — #cybersecurity #ctf
((SELECT post_id FROM post WHERE title = 'How I approach CTF recon phase'),              (SELECT tag_id FROM tag WHERE name = 'cybersecurity')),
((SELECT post_id FROM post WHERE title = 'How I approach CTF recon phase'),              (SELECT tag_id FROM tag WHERE name = 'ctf')),

-- Post 8: hannah_s9 profile — #mcat #premed
((SELECT post_id FROM post WHERE title = 'MCAT prep — 6 months out'),                   (SELECT tag_id FROM tag WHERE name = 'mcat')),
((SELECT post_id FROM post WHERE title = 'MCAT prep — 6 months out'),                   (SELECT tag_id FROM tag WHERE name = 'premed')),

-- Post 9: ivan_mk profile — #formulasae #engineering
((SELECT post_id FROM post WHERE title = 'Sponsor pitch for Formula SAE went well'),    (SELECT tag_id FROM tag WHERE name = 'formulasae')),
((SELECT post_id FROM post WHERE title = 'Sponsor pitch for Formula SAE went well'),    (SELECT tag_id FROM tag WHERE name = 'engineering')),

-- Post 10: jasmine_hl profile — #music #artificialintelligence
((SELECT post_id FROM post WHERE title = 'AI-generated music is getting good — and weird'), (SELECT tag_id FROM tag WHERE name = 'music')),
((SELECT post_id FROM post WHERE title = 'AI-generated music is getting good — and weird'), (SELECT tag_id FROM tag WHERE name = 'artificialintelligence')),

-- Post 11: kevin_nb profile — #databases #java
((SELECT post_id FROM post WHERE title = 'Indexing saved my query from 4s to 40ms'),    (SELECT tag_id FROM tag WHERE name = 'databases')),
((SELECT post_id FROM post WHERE title = 'Indexing saved my query from 4s to 40ms'),    (SELECT tag_id FROM tag WHERE name = 'java')),

-- Post 12: laura_fd profile — #ux #webdev
((SELECT post_id FROM post WHERE title = 'Accessibility checklist I use on every project'), (SELECT tag_id FROM tag WHERE name = 'ux')),
((SELECT post_id FROM post WHERE title = 'Accessibility checklist I use on every project'), (SELECT tag_id FROM tag WHERE name = 'webdev')),

-- Post 13: miguel_cr profile — #springboot #fullstack
((SELECT post_id FROM post WHERE title = 'Deployed my first Spring Boot app to Render'), (SELECT tag_id FROM tag WHERE name = 'springboot')),
((SELECT post_id FROM post WHERE title = 'Deployed my first Spring Boot app to Render'), (SELECT tag_id FROM tag WHERE name = 'fullstack')),

-- Post 14: natalie_bw profile — #ux #edtech
((SELECT post_id FROM post WHERE title = 'Why I switched from Figma to FigJam for early wireframes'), (SELECT tag_id FROM tag WHERE name = 'ux')),
((SELECT post_id FROM post WHERE title = 'Why I switched from Figma to FigJam for early wireframes'), (SELECT tag_id FROM tag WHERE name = 'edtech')),

-- Post 15: omar_hs profile — #cybersecurity #ctf
((SELECT post_id FROM post WHERE title = 'Reverse engineering a login form for practice'), (SELECT tag_id FROM tag WHERE name = 'cybersecurity')),
((SELECT post_id FROM post WHERE title = 'Reverse engineering a login form for practice'), (SELECT tag_id FROM tag WHERE name = 'ctf')),

-- Post 16: paula_gm profile — #premed
((SELECT post_id FROM post WHERE title = 'Research shadowing experience — week 1'),     (SELECT tag_id FROM tag WHERE name = 'premed')),

-- Post 17: quinn_aj profile — #edtech #react
((SELECT post_id FROM post WHERE title = 'Building a quiz app for my EdTech portfolio'), (SELECT tag_id FROM tag WHERE name = 'edtech')),
((SELECT post_id FROM post WHERE title = 'Building a quiz app for my EdTech portfolio'), (SELECT tag_id FROM tag WHERE name = 'react')),

-- Post 18: raj_pt profile — #machinelearning #python
((SELECT post_id FROM post WHERE title = 'Scikit-learn vs PyTorch for a classification task'), (SELECT tag_id FROM tag WHERE name = 'machinelearning')),
((SELECT post_id FROM post WHERE title = 'Scikit-learn vs PyTorch for a classification task'), (SELECT tag_id FROM tag WHERE name = 'python')),

-- Post 19: sofia_er profile — #music #machinelearning
((SELECT post_id FROM post WHERE title = 'How music theory helps me think about ML model structure'), (SELECT tag_id FROM tag WHERE name = 'music')),
((SELECT post_id FROM post WHERE title = 'How music theory helps me think about ML model structure'), (SELECT tag_id FROM tag WHERE name = 'machinelearning')),

-- Post 20: tyler_mc profile — #java #fullstack
((SELECT post_id FROM post WHERE title = 'Git workflow for a 3-person backend team'),   (SELECT tag_id FROM tag WHERE name = 'java')),
((SELECT post_id FROM post WHERE title = 'Git workflow for a 3-person backend team'),   (SELECT tag_id FROM tag WHERE name = 'fullstack')),

-- Community Post 21: alex_m92 in CS_Capstone — #java #databases
((SELECT post_id FROM post WHERE title = 'Capstone milestone 2 checklist'),             (SELECT tag_id FROM tag WHERE name = 'java')),
((SELECT post_id FROM post WHERE title = 'Capstone milestone 2 checklist'),             (SELECT tag_id FROM tag WHERE name = 'databases')),

-- Community Post 22: kevin_nb in CS_Capstone — #java #springboot
((SELECT post_id FROM post WHERE title = 'Should we use JWT or session-based auth?'),   (SELECT tag_id FROM tag WHERE name = 'java')),
((SELECT post_id FROM post WHERE title = 'Should we use JWT or session-based auth?'),   (SELECT tag_id FROM tag WHERE name = 'springboot')),

-- Community Post 23: brianna_t in DataScience_Hub — #python #machinelearning
((SELECT post_id FROM post WHERE title = 'Free datasets for your ML projects'),         (SELECT tag_id FROM tag WHERE name = 'python')),
((SELECT post_id FROM post WHERE title = 'Free datasets for your ML projects'),         (SELECT tag_id FROM tag WHERE name = 'machinelearning')),

-- Community Post 24: raj_pt in DataScience_Hub — #machinelearning #datavisualization
((SELECT post_id FROM post WHERE title = 'Overfitting — how do you actually catch it early?'), (SELECT tag_id FROM tag WHERE name = 'machinelearning')),
((SELECT post_id FROM post WHERE title = 'Overfitting — how do you actually catch it early?'), (SELECT tag_id FROM tag WHERE name = 'datavisualization')),

-- Community Post 25: carlos_v in FullStack_Dev — #springboot #react #fullstack
((SELECT post_id FROM post WHERE title = 'Spring Boot + React setup guide'),            (SELECT tag_id FROM tag WHERE name = 'springboot')),
((SELECT post_id FROM post WHERE title = 'Spring Boot + React setup guide'),            (SELECT tag_id FROM tag WHERE name = 'react')),
((SELECT post_id FROM post WHERE title = 'Spring Boot + React setup guide'),            (SELECT tag_id FROM tag WHERE name = 'fullstack')),

-- Community Post 26: laura_fd in FullStack_Dev — #webdev #react
((SELECT post_id FROM post WHERE title = 'CSS Modules vs Tailwind — which do you prefer?'), (SELECT tag_id FROM tag WHERE name = 'webdev')),
((SELECT post_id FROM post WHERE title = 'CSS Modules vs Tailwind — which do you prefer?'), (SELECT tag_id FROM tag WHERE name = 'react')),

-- Community Post 27: diana_wu in Robotics_Club — #robotics #engineering
((SELECT post_id FROM post WHERE title = 'Demo day prep — what to bring'),              (SELECT tag_id FROM tag WHERE name = 'robotics')),
((SELECT post_id FROM post WHERE title = 'Demo day prep — what to bring'),              (SELECT tag_id FROM tag WHERE name = 'engineering')),

-- Community Post 28: ivan_mk in Robotics_Club — #robotics #engineering
((SELECT post_id FROM post WHERE title = 'Sensor fusion approach for obstacle detection'), (SELECT tag_id FROM tag WHERE name = 'robotics')),
((SELECT post_id FROM post WHERE title = 'Sensor fusion approach for obstacle detection'), (SELECT tag_id FROM tag WHERE name = 'engineering')),

-- Community Post 29: ethan_rc in EdTech_Builders — #edtech #webdev
((SELECT post_id FROM post WHERE title = 'User research findings — student attention span'), (SELECT tag_id FROM tag WHERE name = 'edtech')),
((SELECT post_id FROM post WHERE title = 'User research findings — student attention span'), (SELECT tag_id FROM tag WHERE name = 'webdev')),

-- Community Post 30: quinn_aj in EdTech_Builders — #edtech #react
((SELECT post_id FROM post WHERE title = 'Gamification ideas for our quiz platform'),   (SELECT tag_id FROM tag WHERE name = 'edtech')),
((SELECT post_id FROM post WHERE title = 'Gamification ideas for our quiz platform'),   (SELECT tag_id FROM tag WHERE name = 'react')),

-- Community Post 31: fatima_ok in UX_Design_CSUN — #ux
((SELECT post_id FROM post WHERE title = 'Portfolio review — feedback welcome'),         (SELECT tag_id FROM tag WHERE name = 'ux')),

-- Community Post 32: natalie_bw in UX_Design_CSUN — #ux #webdev
((SELECT post_id FROM post WHERE title = 'Figma prototyping tips I wish I knew earlier'), (SELECT tag_id FROM tag WHERE name = 'ux')),
((SELECT post_id FROM post WHERE title = 'Figma prototyping tips I wish I knew earlier'), (SELECT tag_id FROM tag WHERE name = 'webdev')),

-- Community Post 33: george_lp in CyberSec_CTF — #ctf #cybersecurity
((SELECT post_id FROM post WHERE title = 'CTF team formation for PicoCTF'),             (SELECT tag_id FROM tag WHERE name = 'ctf')),
((SELECT post_id FROM post WHERE title = 'CTF team formation for PicoCTF'),             (SELECT tag_id FROM tag WHERE name = 'cybersecurity')),

-- Community Post 34: omar_hs in CyberSec_CTF — #ctf #cybersecurity
((SELECT post_id FROM post WHERE title = 'Write-up: last week''s binary exploitation challenge'), (SELECT tag_id FROM tag WHERE name = 'ctf')),
((SELECT post_id FROM post WHERE title = 'Write-up: last week''s binary exploitation challenge'), (SELECT tag_id FROM tag WHERE name = 'cybersecurity')),

-- Community Post 35: hannah_s9 in PreMed_Network — #mcat #premed
((SELECT post_id FROM post WHERE title = 'MCAT study group — anyone interested?'),      (SELECT tag_id FROM tag WHERE name = 'mcat')),
((SELECT post_id FROM post WHERE title = 'MCAT study group — anyone interested?'),      (SELECT tag_id FROM tag WHERE name = 'premed')),

-- Community Post 36: sofia_er in PreMed_Network — #premed
((SELECT post_id FROM post WHERE title = 'Research opportunity — neuroscience lab looking for volunteers'), (SELECT tag_id FROM tag WHERE name = 'premed')),

-- Community Post 37: ivan_mk in Formula_SAE — #formulasae #engineering
((SELECT post_id FROM post WHERE title = 'Suspension geometry update — new caster angle'), (SELECT tag_id FROM tag WHERE name = 'formulasae')),
((SELECT post_id FROM post WHERE title = 'Suspension geometry update — new caster angle'), (SELECT tag_id FROM tag WHERE name = 'engineering')),

-- Community Post 38: miguel_cr in Formula_SAE — #formulasae #engineering
((SELECT post_id FROM post WHERE title = 'CAD files updated on shared drive'),           (SELECT tag_id FROM tag WHERE name = 'formulasae')),
((SELECT post_id FROM post WHERE title = 'CAD files updated on shared drive'),           (SELECT tag_id FROM tag WHERE name = 'engineering')),

-- Community Post 39: jasmine_hl in Music_And_AI — #artificialintelligence #machinelearning
((SELECT post_id FROM post WHERE title = 'Paper share: music generation with transformers'), (SELECT tag_id FROM tag WHERE name = 'artificialintelligence')),
((SELECT post_id FROM post WHERE title = 'Paper share: music generation with transformers'), (SELECT tag_id FROM tag WHERE name = 'machinelearning')),

-- Community Post 40: sofia_er in Music_And_AI — #artificialintelligence #music
((SELECT post_id FROM post WHERE title = 'Anyone tried fine-tuning a model on their own compositions?'), (SELECT tag_id FROM tag WHERE name = 'artificialintelligence')),
((SELECT post_id FROM post WHERE title = 'Anyone tried fine-tuning a model on their own compositions?'), (SELECT tag_id FROM tag WHERE name = 'music'));


-- ============================================================
-- Verification
-- ============================================================

-- All posts with author and community
SELECT p.post_id, u.username AS author, c.community_name, p.title, p.like_count, p.comment_count, p.created_at
FROM post p
JOIN users u ON p.author_id = u.user_id
LEFT JOIN community c ON p.community_id = c.community_id
ORDER BY p.created_at DESC;

-- Profile posts only
SELECT p.post_id, u.username, p.title
FROM post p
JOIN users u ON p.author_id = u.user_id
WHERE p.community_id IS NULL
ORDER BY p.post_id;

-- Community posts only
SELECT p.post_id, u.username, c.community_name, p.title
FROM post p
JOIN users u ON p.author_id = u.user_id
JOIN community c ON p.community_id = c.community_id
ORDER BY c.community_name;

-- Tags per post
SELECT p.title, t.name AS tag
FROM post_tag pt
JOIN post p ON pt.post_id = p.post_id
JOIN tag t ON pt.tag_id = t.tag_id
ORDER BY p.post_id;
