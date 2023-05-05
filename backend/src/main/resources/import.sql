INSERT INTO Video (id, title, description, path, duration, views) VALUES (1, 'Java: Introdução e Ambiente de Desenvolvimento', 'Neste vídeo, conheça a linguagem Java e aprenda a configurar o ambiente de desenvolvimento', null, 180, 1010);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (2, 'Java: Sintaxe Básica e Variáveis', 'Aprenda a sintaxe básica do Java e como declarar e utilizar variáveis', null, 240, 502);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (3, 'Java: Estruturas Condicionais e de Repetição', 'Entenda as estruturas condicionais e de repetição no Java e como usá-las em seu código', null, 300, 323);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (4, 'Java: Arrays e Coleções', 'Descubra como trabalhar com arrays e coleções no Java para armazenar e manipular dados', null, 120, 523);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (5, 'Java: Orientação a Objetos - Parte 1', 'Introdução aos conceitos fundamentais da orientação a objetos no Java', null, 90, 43);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (6, 'Java: Orientação a Objetos - Parte 2', 'Continue explorando os conceitos da orientação a objetos no Java com exemplos práticos', null, 150, 52);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (7, 'Java: Exceções e Tratamento de Erros', 'Aprenda a lidar com exceções e realizar tratamento de erros em suas aplicações Java', null, 210, 516);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (8, 'Java: Trabalhando com Arquivos e I/O', 'Entenda como manipular arquivos e realizar operações de entrada e saída no Java', null, 180, 672);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (9, 'Java: Conexão com Banco de Dados', 'Aprenda a conectar seu aplicativo Java a bancos de dados e realizar operações CRUD', null, 240, 123);
INSERT INTO Video (id, title, description, path, duration, views) VALUES (10, 'Java: Desenvolvimento Web com Servlets e JSP', 'Conheça o desenvolvimento web em Java com Servlets e JavaServer Pages (JSP)', null, 300, 32);

INSERT INTO resourcepath(id, path, resolution) values (7, '/home/luan/Vídeos/midia/331', 'HD');
INSERT INTO resourcepath(id, path, resolution) values (8, '/home/luan/Vídeos/midia/331', 'SD');
INSERT INTO resourcepath(id, path, resolution) values (9, '/home/luan/Vídeos/midia/331', 'HD');

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

