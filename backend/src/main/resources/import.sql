INSERT INTO Video (id, title, description, path, duration, views) VALUES (1, 'Video 1', 'Descrição do vídeo 1', null, 180, 1010);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (2, 'Video 2', 'Descrição do vídeo 2', null, 240, 502);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (3, 'Video 3', 'Descrição do vídeo 3', null, 300, 323);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (4, 'Video 4', 'Descrição do vídeo 4', null, 120, 523);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (5, 'Video 5', 'Descrição do vídeo 5', null, 90, 43);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (6, 'Video 6', 'Descrição do vídeo 6', null, 150, 52);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (7, 'Video 7', 'Descrição do vídeo 7', null, 210, 516);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (8, 'Video 8', 'Descrição do vídeo 8', null, 180, 672);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (9, 'Video 9', 'Descrição do vídeo 9', null, 240, 123);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (10, 'Video 10', 'Descrição do vídeo 10', null, 300, 32);

INSERT INTO resourcepath(id, path, resolution) values (1, '/home/luan/Vídeos/midia/331', 'HD');
INSERT INTO resourcepath(id, path, resolution) values (2, '/home/luan/Vídeos/midia/331', 'SD');
INSERT INTO resourcepath(id, path, resolution) values (3, '/home/luan/Vídeos/midia/331', 'HD');

INSERT INTO video_resourcepath (video_id, resolutionpaths_id)
values (1, 1);

INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (100, 1, 100, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (101, 1, 5, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (102, 2, 20, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (103, 2, 8, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (104, 3, 15, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (6, 1, 10, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (7, 1, 8, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (8, 2, 18, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (9, 2, 15, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (10, 3, 22, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (11, 3, 28, '2023-03-22');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (12, 4, 11, '2023-03-05');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (13, 4, 120, '2023-03-06');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (14, 5, 43, '2023-03-05');
INSERT INTO VideoMetrics (id, video_id, views, date) VALUES (15, 5, 90, '2023-03-06');

