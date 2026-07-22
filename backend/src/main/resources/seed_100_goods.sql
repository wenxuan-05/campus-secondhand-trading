-- ============================================
-- 淘鸭 - Campus Secondhand Trading Platform
-- Seed Data: 50 Users + 100 Goods
-- School: 西南财经大学 (柳林校区)
-- Generated: 2026-07-21
-- ============================================

-- ============================================
-- Part 1: Insert 50 Users
-- Password: e10adc3949ba59abbe56e057f20f883e (123456)
-- ============================================
IF NOT EXISTS (SELECT 1 FROM users WHERE student_id = N'20230001')
BEGIN
    INSERT INTO users (student_id, school_name, nickname, avatar, password, phone, real_name, email, gender, dormitory, room_number, credit_score, credit_level, trade_count, good_rate, last_login_time, status, role, is_campus_ambassador, is_merchant, token_version)
    VALUES
(N'20230001', N'西南财经大学', N'柳林小书虫', N'https://picsum.photos/seed/avatar01/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001001', N'张明远', N'zhangmy@swufe.edu.cn', 1, N'柳林1号楼', N'101', 95, N'优秀', 12, 98.5, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230002', N'西南财经大学', N'西湖边的猫', N'https://picsum.photos/seed/avatar02/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001002', N'李雨桐', N'liyt@swufe.edu.cn', 2, N'柳林3号楼', N'205', 88, N'良好', 8, 96.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230003', N'西南财经大学', N'考研上岸鸭', N'https://picsum.photos/seed/avatar03/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001003', N'王浩然', N'wanghr@swufe.edu.cn', 1, N'柳林2号楼', N'312', 90, N'优秀', 15, 99.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230004', N'西南财经大学', N'数码控阿杰', N'https://picsum.photos/seed/avatar04/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001004', N'陈俊杰', N'chenjj@swufe.edu.cn', 1, N'柳林校区7舍', N'418', 82, N'良好', 5, 94.0, DATEADD(DAY, -5, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230005', N'西南财经大学', N'小裙子女王', N'https://picsum.photos/seed/avatar05/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001005', N'赵思琪', N'zhaosq@swufe.edu.cn', 2, N'柳林5号楼', N'520', 91, N'优秀', 20, 97.5, DATEADD(DAY, 0, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230006', N'西南财经大学', N'篮球少年小刘', N'https://picsum.photos/seed/avatar06/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001006', N'刘子轩', N'liuzx@swufe.edu.cn', 1, N'柳林校区10舍', N'203', 78, N'良好', 6, 92.0, DATEADD(DAY, -3, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230007', N'西南财经大学', N'文艺小清新', N'https://picsum.photos/seed/avatar07/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001007', N'孙艺涵', N'sunyh@swufe.edu.cn', 2, N'柳林4号楼', N'608', 85, N'良好', 10, 95.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230008', N'西南财经大学', N'健身达人阿强', N'https://picsum.photos/seed/avatar08/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001008', N'周志强', N'zhouzq@swufe.edu.cn', 1, N'柳林校区8舍', N'115', 70, N'一般', 3, 88.0, DATEADD(DAY, -7, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230009', N'西南财经大学', N'美术生小雨', N'https://picsum.photos/seed/avatar09/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001009', N'吴雨霏', N'wuyf@swufe.edu.cn', 2, N'柳林6号楼', N'702', 92, N'优秀', 18, 98.0, DATEADD(DAY, -1, GETDATE()), 1, N'CAMPUS_AMBASSADOR', 1, 0, 0),
(N'20230010', N'西南财经大学', N'程序员小郑', N'https://picsum.photos/seed/avatar10/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13800001010', N'郑凯文', N'zhengkw@swufe.edu.cn', 1, N'柳林校区11舍', N'501', 88, N'良好', 7, 96.5, DATEADD(DAY, -4, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230011', N'西南财经大学', N'柳林电竞王', N'https://picsum.photos/seed/avatar11/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13900001001', N'钱一鸣', N'qianym@swufe.edu.cn', 1, N'柳林校区12号楼', N'304', 80, N'良好', 9, 93.5, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230012', N'西南财经大学', N'摄影爱好者', N'https://picsum.photos/seed/avatar12/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13900001002', N'林雅婷', N'linyt@swufe.edu.cn', 2, N'柳林校区15号楼', N'412', 94, N'优秀', 14, 99.0, DATEADD(DAY, 0, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230013', N'西南财经大学', N'二次元阿宅', N'https://picsum.photos/seed/avatar13/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13900001003', N'黄梓豪', N'huangzh@swufe.edu.cn', 1, N'柳林校区13号楼', N'218', 65, N'一般', 2, 85.0, DATEADD(DAY, -10, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230014', N'西南财经大学', N'吃货小分队', N'https://picsum.photos/seed/avatar14/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13900001004', N'杨欣怡', N'yangxy@swufe.edu.cn', 2, N'柳林校区14号楼', N'605', 87, N'良好', 11, 95.5, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230015', N'西南财经大学', N'代码搬运工', N'https://picsum.photos/seed/avatar15/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13900001005', N'许文博', N'xuwb@swufe.edu.cn', 1, N'柳林校区16号楼', N'320', 83, N'良好', 6, 94.0, DATEADD(DAY, -3, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230016', N'西南财经大学', N'工大二手王', N'https://picsum.photos/seed/avatar16/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13700001001', N'何嘉伟', N'hejw@swufe.edu.cn', 1, N'柳林校区3号楼', N'108', 90, N'优秀', 25, 97.0, DATEADD(DAY, -1, GETDATE()), 1, N'CAMPUS_AMBASSADOR', 1, 0, 0),
(N'20230017', N'西南财经大学', N'化学系小师姐', N'https://picsum.photos/seed/avatar17/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13700001002', N'吕晓萌', N'lvxm@swufe.edu.cn', 2, N'柳林校区5号楼', N'506', 86, N'良好', 8, 96.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230018', N'西南财经大学', N'足球小将', N'https://picsum.photos/seed/avatar18/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13700001003', N'施博文', N'shibw@swufe.edu.cn', 1, N'柳林校区8号楼', N'210', 76, N'一般', 4, 90.0, DATEADD(DAY, -5, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230019', N'西南财经大学', N'考研在逃公主', N'https://picsum.photos/seed/avatar19/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13700001004', N'张雪莹', N'zhangxy2@swufe.edu.cn', 2, N'柳林校区6号楼', N'425', 89, N'良好', 7, 95.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230020', N'西南财经大学', N'机械键盘侠', N'https://picsum.photos/seed/avatar20/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13700001005', N'蔡明哲', N'caimz@swufe.edu.cn', 1, N'柳林校区4号楼', N'315', 81, N'良好', 5, 93.0, DATEADD(DAY, -3, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230021', N'西南财经大学', N'商大经济学人', N'https://picsum.photos/seed/avatar21/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13600001001', N'潘俊豪', N'panjh@swufe.edu.cn', 1, N'柳林校区20号楼', N'228', 93, N'优秀', 13, 98.5, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230022', N'西南财经大学', N'奶茶重度患者', N'https://picsum.photos/seed/avatar22/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13600001002', N'杜婉清', N'duwq@swufe.edu.cn', 2, N'柳林校区22号楼', N'612', 84, N'良好', 6, 94.5, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230023', N'西南财经大学', N'吉他弹唱歌手', N'https://picsum.photos/seed/avatar23/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13600001003', N'任天宇', N'renty@swufe.edu.cn', 1, N'柳林校区21号楼', N'401', 79, N'一般', 3, 89.0, DATEADD(DAY, -8, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230024', N'西南财经大学', N'法考备战中', N'https://picsum.photos/seed/avatar24/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13600001004', N'姜思远', N'jiangsy@swufe.edu.cn', 1, N'柳林校区23号楼', N'118', 91, N'优秀', 16, 97.5, DATEADD(DAY, 0, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230025', N'西南财经大学', N'日系穿搭控', N'https://picsum.photos/seed/avatar25/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13600001005', N'苏小曼', N'suxm@swufe.edu.cn', 2, N'柳林校区24号楼', '709', 88, N'良好', 9, 96.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230026', N'西南财经大学', N'服设小裁缝', N'https://picsum.photos/seed/avatar26/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13500001001', N'方晓彤', N'fangxt@swufe.edu.cn', 2, N'柳林校区8号楼', N'316', 86, N'良好', 7, 95.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230027', N'西南财经大学', N'田径队小飞人', N'https://picsum.photos/seed/avatar27/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13500001002', N'袁鹏飞', N'yuanpf@swufe.edu.cn', 1, N'柳林校区6号楼', N'202', 77, N'一般', 4, 91.0, DATEADD(DAY, -6, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230028', N'西南财经大学', N'手账本控', N'https://picsum.photos/seed/avatar28/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13500001003', N'田佳妮', N'tianjn@swufe.edu.cn', 2, N'柳林校区9号楼', N'504', 90, N'优秀', 12, 98.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230029', N'西南财经大学', N'摄影器材党', N'https://picsum.photos/seed/avatar29/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13500001004', N'丁浩然', N'dinghr@swufe.edu.cn', 1, N'柳林校区7号楼', N'422', 85, N'良好', 8, 95.5, DATEADD(DAY, -3, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230030', N'西南财经大学', N'耳机发烧友', N'https://picsum.photos/seed/avatar30/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13500001005', N'马思源', N'masy@swufe.edu.cn', 1, N'柳林校区5号楼', N'108', 82, N'良好', 5, 94.0, DATEADD(DAY, -4, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230031', N'西南财经大学', N'师范小老师', N'https://picsum.photos/seed/avatar31/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13400001001', N'沈佳琪', N'shenjq@swufe.edu.cn', 2, N'柳林校区12号楼', N'305', 92, N'优秀', 14, 98.5, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230032', N'西南财经大学', N'游泳健将', N'https://picsum.photos/seed/avatar32/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13400001002', N'傅子豪', N'fuzh@swufe.edu.cn', 1, N'柳林校区14号楼', N'218', 80, N'良好', 6, 93.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230033', N'西南财经大学', N'汉服爱好者', N'https://picsum.photos/seed/avatar33/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13400001003', N'秦若兰', N'qinrl@swufe.edu.cn', 2, N'柳林校区13号楼', N'608', 87, N'良好', 9, 96.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230034', N'西南财经大学', N'英语角常驻', N'https://picsum.photos/seed/avatar34/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13400001004', N'孔维杰', N'kongwj@swufe.edu.cn', 1, N'柳林校区15号楼', N'410', 89, N'良好', 11, 97.0, DATEADD(DAY, -3, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230035', N'西南财经大学', N'德云社在逃弟子', N'https://picsum.photos/seed/avatar35/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13400001005', N'曹宇轩', N'caoyx@swufe.edu.cn', 1, N'柳林校区16号楼', N'322', 75, N'一般', 3, 87.5, DATEADD(DAY, -5, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230036', N'西南财经大学', N'计量小能手', N'https://picsum.photos/seed/avatar36/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13300001001', N'魏子涵', N'weizh@swufe.edu.cn', 1, N'柳林校区10号楼', N'206', 88, N'良好', 7, 95.5, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230037', N'西南财经大学', N'瑜伽小达人', N'https://picsum.photos/seed/avatar37/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13300001002', N'蒋欣冉', N'jiangxr@swufe.edu.cn', 2, N'柳林校区11号楼', N'512', 91, N'优秀', 15, 98.0, DATEADD(DAY, 0, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230038', N'西南财经大学', N'滑板少年', N'https://picsum.photos/seed/avatar38/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13300001003', N'沈逸飞', N'shenyf@swufe.edu.cn', 1, N'柳林校区9号楼', N'118', 72, N'一般', 2, 86.0, DATEADD(DAY, -9, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230039', N'西南财经大学', N'考证达人', N'https://picsum.photos/seed/avatar39/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13300001004', N'韩雪晴', N'hanxq@swufe.edu.cn', 2, N'柳林校区12号楼', N'701', 94, N'优秀', 19, 99.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230040', N'西南财经大学', N'3D打印狂魔', N'https://picsum.photos/seed/avatar40/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13300001005', N'彭博远', N'pengby@swufe.edu.cn', 1, N'柳林校区8号楼', N'405', 83, N'良好', 6, 94.0, DATEADD(DAY, -3, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230041', N'西南财经大学', N'财大CPAer', N'https://picsum.photos/seed/avatar41/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13200001001', N'范嘉诚', N'fanjc@swufe.edu.cn', 1, N'柳林校区18号楼', N'312', 90, N'优秀', 10, 97.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230042', N'西南财经大学', N'美妆博主小号', N'https://picsum.photos/seed/avatar42/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13200001002', N'唐诗涵', N'tangsh@swufe.edu.cn', 2, N'柳林校区19号楼', N'618', 87, N'良好', 8, 96.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230043', N'西南财经大学', N'股市小韭菜', N'https://picsum.photos/seed/avatar43/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13200001003', N'曾子航', N'zengzh@swufe.edu.cn', 1, N'柳林校区17号楼', N'220', 78, N'一般', 4, 90.0, DATEADD(DAY, -6, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230044', N'西南财经大学', N'文具控', N'https://picsum.photos/seed/avatar44/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13200001004', N'陆思雨', N'lusy@swufe.edu.cn', 2, N'柳林校区20号楼', N'503', 93, N'优秀', 17, 98.5, DATEADD(DAY, 0, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230045', N'西南财经大学', N'雅思7分选手', N'https://picsum.photos/seed/avatar45/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13200001005', N'邓嘉懿', N'dengjy@swufe.edu.cn', 1, N'柳林校区16号楼', N'411', 92, N'优秀', 12, 98.0, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),

(N'20230046', N'西南财经大学', N'播音系小哥哥', N'https://picsum.photos/seed/avatar46/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13100001001', N'肖俊熙', N'xiaojx@swufe.edu.cn', 1, N'柳林校区5号楼', N'308', 85, N'良好', 6, 95.0, DATEADD(DAY, -2, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230047', N'西南财经大学', N'剪辑小能手', N'https://picsum.photos/seed/avatar47/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13100001002', N'冯思涵', N'fengsh@swufe.edu.cn', 2, N'柳林校区6号楼', N'520', 86, N'良好', 7, 96.5, DATEADD(DAY, -1, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230048', N'西南财经大学', N'剧本杀DM', N'https://picsum.photos/seed/avatar48/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13100001003', N'董浩然', N'donghr@swufe.edu.cn', 1, N'柳林校区4号楼', N'216', 76, N'一般', 3, 88.0, DATEADD(DAY, -7, GETDATE()), 1, N'USER', 0, 0, 0),
(N'20230049', N'西南财经大学', N'追星前线', N'https://picsum.photos/seed/avatar49/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13100001004', N'崔晓雯', N'cuixw@swufe.edu.cn', 2, N'柳林校区7号楼', N'625', 84, N'良好', 5, 93.5, DATEADD(DAY, -3, GETDATE()), 1, N'CAMPUS_AMBASSADOR', 1, 0, 0),
(N'20230050', N'西南财经大学', N'Vlog博主', N'https://picsum.photos/seed/avatar50/200/200', N'e10adc3949ba59abbe56e057f20f883e', N'13100001005', N'尤天乐', N'youtl@swufe.edu.cn', 1, N'柳林校区3号楼', N'102', 80, N'良好', 4, 92.0, DATEADD(DAY, -4, GETDATE()), 1, N'USER', 0, 0, 0);

    PRINT N'50 users inserted successfully.';
END
ELSE
BEGIN
    PRINT N'50 users already exist, skipping.';
END

-- ============================================
-- Part 2: Insert 100 Goods
-- Category distribution:
--   书籍教材(20), 电子产品(20), 生活用品(15), 衣物鞋帽(10),
--   运动器材(10), 美妆护肤(5), 乐器(5), 数码配件(10), 其他(5)
-- user_id range: 3~52 (the 50 users just inserted)
-- ============================================

-- Note: user_id values use a formula to evenly distribute goods among users
-- Each user gets ~2 goods on average
-- We'll assign user_ids ranging from 3 to 52

IF NOT EXISTS (SELECT 1 FROM goods WHERE title = N'高等数学 第七版 上下册 同济大学')
BEGIN
    INSERT INTO goods (user_id, title, description, price, original_price, condition_level, category, images, video, isbn, location, tags, collect_count, chat_count, negotiable, shipping, school_id, status, view_count, expire_time, created_at)
    VALUES
-- ============================================
-- 书籍教材 (20 items: #1-#20)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230001'), N'高等数学 第七版 上下册 同济大学', N'同济大学数学系编，上下册一起出。只用了大一上学期，上册有少量笔记（铅笔写的可以擦掉），下册基本全新。书角无折痕，非常适合学弟学妹。', 30.00, 78.00, 2, N'book',
 N'["https://picsum.photos/seed/math-book-1/400/400","https://picsum.photos/seed/math-book-2/400/400"]',
 N'', N'9787040396638', N'{"id":"lib","name":"图书馆","building":"图书馆一楼大厅"}', N'["教材","数学","考研"]', 15, 8, 1, 0, 1, 2, 245, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230002'), N'线性代数 同济第六版 全新未用', N'选课选错了多买了一本，塑封都没拆，全新。同济大学版的线性代数，工科必备。', 15.00, 32.00, 1, N'book',
 N'["https://picsum.photos/seed/linear-algebra-1/400/400","https://picsum.photos/seed/linear-algebra-2/400/400"]',
 N'', N'9787040396614', N'{"id":"dorm","name":"宿舍楼下","building":"柳林1号楼"}', N'["教材","数学","全新"]', 8, 3, 0, 0, 1, 2, 132, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230003'), N'概率论与数理统计 浙大第四版', N'盛骤编，经典教材。这本书帮我概率论拿了A，习题答案用铅笔写在空白处，不影响阅读。附送自己整理的复习笔记一份。', 12.00, 35.80, 3, N'book',
 N'["https://picsum.photos/seed/probability-book-1/400/400","https://picsum.photos/seed/probability-book-2/400/400"]',
 N'', N'9787040396607', N'{"id":"canteen","name":"食堂门口","building":"柳林食堂一楼"}', N'["教材","数学","笔记"]', 10, 5, 1, 0, 1, 2, 178, DATEADD(DAY, 30, DATEADD(DAY, -8, GETDATE())), DATEADD(DAY, -8, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230004'), N'新东方四级词汇 乱序版 绿皮', N'背了大约1/3，前面有些荧光笔标记。书角稍微有点卷，但里面很干净。自带词根+联想记忆法，非常实用。', 8.00, 29.80, 4, N'book',
 N'["https://picsum.photos/seed/cet4-book-1/400/400","https://picsum.photos/seed/cet4-book-2/400/400"]',
 N'', N'9787553628187', N'{"id":"lib","name":"图书馆","building":"图书馆二楼自习区"}', N'["英语","四级","词汇"]', 6, 4, 1, 0, 1, 2, 98, DATEADD(DAY, 30, DATEADD(DAY, -10, GETDATE())), DATEADD(DAY, -10, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230005'), N'星火英语 六级真题 2024版 含听力', N'2024年6月最新版，含10套真题+5套模拟，听力扫码听。只做了2套真题，其他都是全新的。送作文模板小册子。', 18.00, 49.80, 2, N'book',
 N'["https://picsum.photos/seed/cet6-book-1/400/400","https://picsum.photos/seed/cet6-book-2/400/400"]',
 N'', N'9787572223456', N'{"id":"classroom","name":"教学楼","building":"东教学楼A区"}', N'["英语","六级","真题"]', 12, 6, 0, 0, 1, 2, 189, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230006'), N'2025考研英语红宝书 词汇必考词', N'考研英语词汇书，红宝书经典版。前20个单元划线过，后30个单元全新。随书附赠精缩版小册子和串记手册。', 20.00, 59.80, 3, N'book',
 N'["https://picsum.photos/seed/red-book-1/400/400","https://picsum.photos/seed/red-book-2/400/400"]',
 N'', N'9787519200015', N'{"id":"lib","name":"图书馆","building":"图书馆三楼自习室"}', N'["考研","英语","词汇"]', 20, 10, 1, 0, 1, 2, 312, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230007'), N'肖秀荣考研政治 精讲精练+1000题', N'2025版的精讲精练和1000题两本一起出。精讲精练前两章有划线，1000题只做了单选部分。考研人必备。', 25.00, 89.00, 3, N'book',
 N'["https://picsum.photos/seed/politics-book-1/400/400","https://picsum.photos/seed/politics-book-2/400/400"]',
 N'', N'9787519200022', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区7舍"}', N'["考研","政治","肖秀荣"]', 18, 9, 1, 0, 1, 2, 267, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230008'), N'数据结构 C语言版 严蔚敏 经典教材', N'408考研必备，计算机专业圣经级教材。全书有认真做的笔记，字迹工整不影响阅读，反而有助于理解。', 15.00, 38.00, 3, N'book',
 N'["https://picsum.photos/seed/ds-book-1/400/400","https://picsum.photos/seed/ds-book-2/400/400"]',
 N'', N'9787302147510', N'{"id":"classroom","name":"教学楼","building":"西教学楼B区"}', N'["教材","计算机","考研"]', 22, 11, 0, 0, 1, 2, 345, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230009'), N'计算机组成原理 唐朔飞 第3版', N'计算机专业核心课教材。书本保存得很好，只有前3章有少量标记。附送期末考试复习资料。', 18.00, 45.00, 2, N'book',
 N'["https://picsum.photos/seed/comp-org-1/400/400","https://picsum.photos/seed/comp-org-2/400/400"]',
 N'', N'9787040538868', N'{"id":"lib","name":"图书馆","building":"图书馆四楼"}', N'["教材","计算机","期末"]', 9, 4, 1, 0, 1, 2, 156, DATEADD(DAY, 30, DATEADD(DAY, -7, GETDATE())), DATEADD(DAY, -7, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230010'), N'操作系统概念 第9版 英文影印版', N'计算机专业必读，英文原版影印。前两章认真读了做了笔记，后面全新。英文阅读对提升专业英语帮助很大。', 22.00, 69.00, 3, N'book',
 N'["https://picsum.photos/seed/os-book-1/400/400","https://picsum.photos/seed/os-book-2/400/400"]',
 N'', N'9787111605782', N'{"id":"lib","name":"图书馆","building":"图书馆三楼"}', N'["教材","计算机","英文"]', 7, 3, 1, 0, 1, 2, 112, DATEADD(DAY, 30, DATEADD(DAY, -9, GETDATE())), DATEADD(DAY, -9, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230011'), N'计算机网络 谢希仁 第8版', N'计网经典教材，课程学完就出了。笔记重点突出，对于期末考试复习非常有用。', 16.00, 42.00, 2, N'book',
 N'["https://picsum.photos/seed/network-book-1/400/400","https://picsum.photos/seed/network-book-2/400/400"]',
 N'', N'9787121390234', N'{"id":"classroom","name":"教学楼","building":"东教学楼C区"}', N'["教材","计算机","网络"]', 11, 5, 0, 0, 1, 2, 198, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230012'), N'曼昆 经济学原理 微观+宏观 第8版', N'经济学入门神作，两本一起卖。微观分册有阅读痕迹，宏观分册基本全新。通俗易懂，非经济专业也能看懂。', 35.00, 118.00, 3, N'book',
 N'["https://picsum.photos/seed/econ-book-1/400/400","https://picsum.photos/seed/econ-book-2/400/400"]',
 N'', N'9787301256893', N'{"id":"lib","name":"图书馆","building":"图书馆二楼"}', N'["教材","经济学","入门"]', 14, 7, 1, 0, 1, 2, 223, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230013'), N'会计学原理 第6版 含习题集', N'基础会计学教材，配合习题集使用效果很好。书上例题都做过了但写得很轻，可以当新书用。', 20.00, 56.00, 3, N'book',
 N'["https://picsum.photos/seed/accounting-book-1/400/400","https://picsum.photos/seed/accounting-book-2/400/400"]',
 N'', N'9787565432156', N'{"id":"classroom","name":"教学楼","building":"东教学楼B区"}', N'["教材","会计","经管"]', 5, 2, 1, 0, 1, 2, 87, DATEADD(DAY, 30, DATEADD(DAY, -11, GETDATE())), DATEADD(DAY, -11, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230014'), N'Python编程 从入门到实践 第3版', N'最棒的Python入门书，没有之一！跟着做完了所有项目（外星人入侵、数据可视化、Web应用），书况很好无折痕。', 28.00, 89.00, 2, N'book',
 N'["https://picsum.photos/seed/python-book-1/400/400","https://picsum.photos/seed/python-book-2/400/400"]',
 N'', N'9787115546081', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["编程","Python","入门"]', 25, 12, 0, 0, 1, 2, 412, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230015'), N'Java核心技术 卷I 第12版 全新', N'Java入门必备，买来发现跟不上课程进度换了别的书看。几乎全新，光盘未拆。', 35.00, 129.00, 1, N'book',
 N'["https://picsum.photos/seed/java-book-1/400/400","https://picsum.photos/seed/java-book-2/400/400"]',
 N'', N'9787111564263', N'{"id":"lib","name":"图书馆","building":"图书馆三楼"}', N'["编程","Java","全新"]', 16, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230016'), N'大学物理学 张三慧 第四版 上下册', N'大学物理通用教材，上下册一起出。书页略有使用痕迹，课后习题有少量铅笔演算。', 22.00, 68.00, 3, N'book',
 N'["https://picsum.photos/seed/physics-book-1/400/400","https://picsum.photos/seed/physics-book-2/400/400"]',
 N'', N'9787302426586', N'{"id":"classroom","name":"教学楼","building":"西教学楼A区"}', N'["教材","物理","理工"]', 4, 2, 1, 0, 1, 2, 76, DATEADD(DAY, 30, DATEADD(DAY, -14, GETDATE())), DATEADD(DAY, -14, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230017'), N'马克思主义基本原理 2023版 教材', N'思政课教材，只用了一学期，基本无笔记。适用于所有高校马原课程。', 8.00, 26.00, 2, N'book',
 N'["https://picsum.photos/seed/marx-book-1/400/400","https://picsum.photos/seed/marx-book-2/400/400"]',
 N'', N'9787040599008', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区3号楼"}', N'["教材","思政","公共课"]', 3, 1, 0, 0, 1, 2, 54, DATEADD(DAY, 30, DATEADD(DAY, -15, GETDATE())), DATEADD(DAY, -15, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230018'), N'新概念英语3 全新未拆封', N'本来想自学买的，结果一直没时间打开...塑封完整，配光盘。英语提升的经典教材。', 15.00, 45.00, 1, N'book',
 N'["https://picsum.photos/seed/nce-book-1/400/400","https://picsum.photos/seed/nce-book-2/400/400"]',
 N'', N'9787560013494', N'{"id":"canteen","name":"食堂门口","building":"柳林校区食堂"}', N'["英语","新概念","全新"]', 7, 3, 1, 0, 1, 2, 109, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

-- ============================================
-- 电子产品 (20 items: #23-#42)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230005'), N'iPad Air 5 M1芯片 64G 深空灰', N'2023年6月购入，主要用于看网课和记笔记。一直贴膜带壳使用，屏幕0划痕，电池健康度96%。考研结束出掉回血，送一个保护壳。', 2800.00, 4799.00, 2, N'electronic',
 N'["https://picsum.photos/seed/ipad-air-1/400/400","https://picsum.photos/seed/ipad-air-2/400/400","https://picsum.photos/seed/ipad-air-3/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林3号楼"}', N'["iPad","苹果","平板","考研"]', 35, 20, 1, 0, 1, 2, 678, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230009'), N'MacBook Air M1 8G+256G 银色 国行', N'2022年双十一入手，主要用来写论文和刷网课。电池循环约120次，键盘无磨损，A面有一个米粒大小的轻微划痕（特定角度才能看到）。原装充电器+原装盒子都在。换了游戏本所以出。', 3800.00, 7999.00, 3, N'electronic',
 N'["https://picsum.photos/seed/macbook-air-1/400/400","https://picsum.photos/seed/macbook-air-2/400/400","https://picsum.photos/seed/macbook-air-3/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆一楼咖啡厅"}', N'["MacBook","苹果","笔记本","轻薄本"]', 42, 25, 1, 0, 1, 2, 892, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230010'), N'iPhone 13 128G 午夜色 国行在保', N'2024年1月京东购入，保修到2025年1月。全程贴膜带壳，屏幕完美，边框无磕碰。电池健康度91%。换了新手机所以出。', 3200.00, 5999.00, 2, N'electronic',
 N'["https://picsum.photos/seed/iphone13-1/400/400","https://picsum.photos/seed/iphone13-2/400/400"]',
 N'', N'', N'{"id":"canteen","name":"食堂门口","building":"柳林校区食堂"}', N'["iPhone","苹果","手机"]', 38, 22, 1, 0, 1, 2, 756, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230011'), N'AirPods Pro 2代 Lightning版', N'2023年10月购入，使用频率不高（主要在图书馆用）。耳机本体无划痕，耳塞已更换为新的M号（送两副备用耳塞）。续航正常，降噪效果依旧出色。换了USB-C版所以出。', 850.00, 1899.00, 2, N'electronic',
 N'["https://picsum.photos/seed/airpods-1/400/400","https://picsum.photos/seed/airpods-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆自习区"}', N'["AirPods","苹果","耳机","降噪"]', 28, 15, 0, 0, 1, 2, 534, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230012'), N'小米平板6 Pro 11寸 8+256G WiFi', N'2024年3月入手，骁龙8+处理器，144Hz高刷屏。主要用来看视频和PDF，成色95新，带原装保护套和67W快充。', 1600.00, 2699.00, 2, N'electronic',
 N'["https://picsum.photos/seed/mipad-1/400/400","https://picsum.photos/seed/mipad-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区16号楼"}', N'["小米","平板","安卓"]', 20, 10, 1, 0, 1, 2, 389, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230013'), N'华为MatePad 11 2023款 柔光屏版', N'2023年9月购入，柔光屏版看PDF真的很舒服像看纸书一样。128G存储，带华为M-Pencil二代手写笔一起出。', 1500.00, 2799.00, 2, N'electronic',
 N'["https://picsum.photos/seed/matepad-1/400/400","https://picsum.photos/seed/matepad-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆考研专区"}', N'["华为","平板","手写笔"]', 25, 12, 1, 0, 1, 2, 467, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230015'), N'戴尔 S2421HN 23.8寸 IPS显示器', N'75Hz刷新率，IPS面板色彩很好。宿舍外接笔记本用，成色95新，屏幕无坏点无划痕。自带HDMI线，送一个显示器增高架。毕业了带不走。', 450.00, 999.00, 2, N'electronic',
 N'["https://picsum.photos/seed/dell-monitor-1/400/400","https://picsum.photos/seed/dell-monitor-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区10舍"}', N'["显示器","戴尔","外设"]', 18, 9, 0, 0, 1, 2, 312, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230016'), N'ikbc C87 机械键盘 Cherry茶轴', N'用了大概半年，Cherry茶轴手感是真的好，打字写代码都很舒服。键帽基本无打油，定期清洁。换了无线键盘所以出。', 180.00, 399.00, 3, N'electronic',
 N'["https://picsum.photos/seed/ikbc-kb-1/400/400","https://picsum.photos/seed/ikbc-kb-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区4号楼"}', N'["机械键盘","ikbc","Cherry","外设"]', 15, 8, 1, 0, 1, 2, 278, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230017'), N'罗技 G502 HERO 游戏鼠标', N'经典电竞鼠标，可调配重，11个可编程按键。用了8个月，微动正常，脚贴轻微磨损但不影响使用。换了GPW所以出。', 100.00, 349.00, 3, N'electronic',
 N'["https://picsum.photos/seed/g502-1/400/400","https://picsum.photos/seed/g502-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区13号楼"}', N'["鼠标","罗技","游戏","外设"]', 12, 6, 1, 0, 1, 2, 198, DATEADD(DAY, 30, DATEADD(DAY, -8, GETDATE())), DATEADD(DAY, -8, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230018'), N'索尼 WH-1000XM4 头戴式降噪耳机', N'考研期间在图书馆用的，降噪效果一流，戴上就是自己的世界。耳罩皮有一点点使用痕迹但不明显。续航约25小时。', 850.00, 2299.00, 3, N'electronic',
 N'["https://picsum.photos/seed/sony-xm4-1/400/400","https://picsum.photos/seed/sony-xm4-2/400/400","https://picsum.photos/seed/sony-xm4-3/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆自习区"}', N'["耳机","索尼","降噪","考研"]', 32, 18, 1, 0, 1, 2, 623, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230019'), N'Kindle Paperwhite 4 8G 黑色', N'考研看论文和电子书用的，墨水屏确实护眼。带官方保护套（蓝色），屏幕完美无划痕。考完研了用不到了。', 380.00, 998.00, 2, N'electronic',
 N'["https://picsum.photos/seed/kindle-1/400/400","https://picsum.photos/seed/kindle-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆三楼"}', N'["Kindle","电子书","亚马逊"]', 22, 11, 0, 0, 1, 2, 401, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230020'), N'小米手环8 NFC版 黑色', N'2024年4月购入，主要用来刷门禁和记录运动。屏幕完美，表带有点使用痕迹（可以自己换表带）。续航一周左右。换了Apple Watch所以出。', 120.00, 299.00, 3, N'electronic',
 N'["https://picsum.photos/seed/miband-1/400/400","https://picsum.photos/seed/miband-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区20号楼"}', N'["小米","手环","运动"]', 10, 5, 1, 0, 1, 2, 167, DATEADD(DAY, 30, DATEADD(DAY, -7, GETDATE())), DATEADD(DAY, -7, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230021'), N'Apple Watch SE 2代 40mm GPS版', N'2023年11月购买，主要用于运动记录和消息提醒。屏幕有一道很浅的划痕（亮屏看不出来），表带是原装运动型表带S/M号。', 1000.00, 1999.00, 3, N'electronic',
 N'["https://picsum.photos/seed/apple-watch-1/400/400","https://picsum.photos/seed/apple-watch-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"体育馆入口"}', N'["Apple Watch","手表","运动"]', 18, 9, 1, 0, 1, 2, 345, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230022'), N'JBL GO3 便携蓝牙音箱 红色', N'洗澡神器！防水防尘，音质比手机外放好太多。用了3个月，成色很新。买了个大音箱所以这个小的出掉。', 150.00, 329.00, 2, N'electronic',
 N'["https://picsum.photos/seed/jbl-go3-1/400/400","https://picsum.photos/seed/jbl-go3-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林2号楼"}', N'["音箱","JBL","蓝牙","防水"]', 8, 4, 1, 0, 1, 2, 145, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230023'), N'罗马仕 20000mAh 充电宝 22.5W快充', N'大容量充电宝，可以给手机充4-5次电。支持PD快充，带数显电量。用了半年，续航基本没有衰减。出门旅行必备。', 45.00, 119.00, 3, N'electronic',
 N'["https://picsum.photos/seed/powerbank-1/400/400","https://picsum.photos/seed/powerbank-2/400/400"]',
 N'', N'', N'{"id":"canteen","name":"食堂门口","building":"柳林校区食堂门口"}', N'["充电宝","快充","数码"]', 14, 7, 1, 1, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230024'), N'卡西欧 fx-991CN X 科学计算器', N'考试神器！可以解方程、矩阵、积分、复数。期末考试和考研都可以用（非编程计算器，考场允许携带）。几乎全新，带盒子说明书。', 60.00, 178.00, 2, N'electronic',
 N'["https://picsum.photos/seed/casio-calc-1/400/400","https://picsum.photos/seed/casio-calc-2/400/400"]',
 N'', N'', N'{"id":"classroom","name":"教学楼","building":"东教学楼考场区"}', N'["计算器","卡西欧","考试"]', 16, 8, 0, 0, 1, 2, 289, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230025'), N'Xbox Series 无线手柄 白色', N'买来在PC上玩游戏的，手感比国产手柄好太多。用了不到半年，摇杆无漂移，按键灵敏。换了精英手柄所以出。', 180.00, 419.00, 2, N'electronic',
 N'["https://picsum.photos/seed/xbox-pad-1/400/400","https://picsum.photos/seed/xbox-pad-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["手柄","Xbox","游戏","PC"]', 20, 11, 1, 0, 1, 2, 367, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230026'), N'Nintendo Switch Lite 黄色', N'2023年买的日版，主要用来玩塞尔达和动森。屏幕贴了钢化膜，机身有轻微使用痕迹。送收纳包一个。换了OLED版所以出。', 650.00, 1299.00, 3, N'electronic',
 N'["https://picsum.photos/seed/switch-lite-1/400/400","https://picsum.photos/seed/switch-lite-2/400/400","https://picsum.photos/seed/switch-lite-3/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区8号楼"}', N'["Switch","任天堂","游戏机"]', 30, 16, 1, 0, 1, 2, 578, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230027'), N'显示器支架 桌上型 承重8kg', N'铝合金材质，气压弹簧调节，支持17-27寸屏幕。装上后桌面清爽多了，脖子也舒服了。用了一年，功能完好。', 60.00, 169.00, 3, N'electronic',
 N'["https://picsum.photos/seed/monitor-arm-1/400/400","https://picsum.photos/seed/monitor-arm-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区3号楼"}', N'["支架","显示器","桌面"]', 12, 6, 1, 0, 1, 2, 203, DATEADD(DAY, 30, DATEADD(DAY, -9, GETDATE())), DATEADD(DAY, -9, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230028'), N'雷蛇 蝰蛇V3 有线鼠标 轻量版', N'59g超轻量，用的Razer Focus Pro传感器，FPS游戏神器。用了3个月，成色95新。换了无线版所以出。', 150.00, 399.00, 2, N'electronic',
 N'["https://picsum.photos/seed/razer-mouse-1/400/400","https://picsum.photos/seed/razer-mouse-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区13号楼"}', N'["鼠标","雷蛇","游戏","FPS"]', 10, 5, 1, 0, 1, 2, 187, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230029'), N'佳能 200D II 单反相机 白色', N'入门级单反，颜值超高，适合拍人像和Vlog。2410万像素，翻转触摸屏。快门数约4000，带18-55套机镜头+相机包+两块电池。换了微单所以出。', 2200.00, 4299.00, 2, N'electronic',
 N'["https://picsum.photos/seed/canon-200d-1/400/400","https://picsum.photos/seed/canon-200d-2/400/400","https://picsum.photos/seed/canon-200d-3/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆门口广场"}', N'["相机","佳能","单反","摄影"]', 40, 20, 1, 0, 1, 2, 789, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230030'), N'小米电子书阅读器 墨水屏 6寸', N'跟Kindle差不多但可以装微信读书，安卓系统更自由。看网文和小说神器，墨水屏不伤眼。用了半年。', 350.00, 699.00, 2, N'electronic',
 N'["https://picsum.photos/seed/mi-reader-1/400/400","https://picsum.photos/seed/mi-reader-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆二楼"}', N'["电子书","小米","墨水屏"]', 15, 7, 1, 0, 1, 2, 256, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

-- ============================================
-- 生活用品 (15 items: #43-#57)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230031'), N'宿舍小冰箱 4L迷你款 冷暖两用', N'夏天冰可乐冬天热牛奶的神器！4L容量刚好够一个人用，能放6罐可乐或者4瓶酸奶。运行声音很小，室友不反感。毕业出。', 70.00, 189.00, 3, N'daily',
 N'["https://picsum.photos/seed/mini-fridge-1/400/400","https://picsum.photos/seed/mini-fridge-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林1号楼"}', N'["冰箱","宿舍","小家电"]', 25, 12, 1, 0, 1, 2, 456, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230032'), N'小熊加湿器 4L大容量 静音款', N'宿舍开空调太干燥了，买了这个加湿器。4L容量开一晚上刚好。超声波雾化很细腻，不会打湿桌面。用了半年。', 35.00, 99.00, 2, N'daily',
 N'["https://picsum.photos/seed/humidifier-1/400/400","https://picsum.photos/seed/humidifier-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林5号楼"}', N'["加湿器","宿舍","小家电"]', 12, 5, 1, 0, 1, 2, 198, DATEADD(DAY, 30, DATEADD(DAY, -7, GETDATE())), DATEADD(DAY, -7, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230033'), N'美的电热水壶 1.5L 304不锈钢', N'宿舍烧水必备，1.5L够泡面+泡茶。304不锈钢内胆，烧水快，自动断电。买了饮水机所以出。', 25.00, 79.00, 3, N'daily',
 N'["https://picsum.photos/seed/kettle-1/400/400","https://picsum.photos/seed/kettle-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区8舍"}', N'["电热水壶","美的","宿舍"]', 8, 4, 0, 0, 1, 2, 134, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230034'), N'宿舍床上小桌板 可折叠 带USB风扇', N'冬天在床上用电脑写作业的神器！可折叠不占地方，桌面带卡槽可以放手机平板。附送一个小USB风扇。毕业清仓。', 30.00, 89.00, 3, N'daily',
 N'["https://picsum.photos/seed/bed-desk-1/400/400","https://picsum.photos/seed/bed-desk-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区14号楼"}', N'["床上桌","宿舍","学习"]', 10, 4, 1, 0, 1, 2, 178, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230035'), N'塑料收纳箱 3件套 大号+中号+小号', N'搬家收纳神器！可以装衣服、书本、杂物。三个箱子可以叠放，不用的时候可以套在一起省空间。用了不到一年。', 35.00, 89.00, 3, N'daily',
 N'["https://picsum.photos/seed/storage-box-1/400/400","https://picsum.photos/seed/storage-box-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区6号楼"}', N'["收纳箱","搬家","整理"]', 6, 3, 1, 0, 1, 2, 98, DATEADD(DAY, 30, DATEADD(DAY, -10, GETDATE())), DATEADD(DAY, -10, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230036'), N'不锈钢晾衣架 落地式 双杆 可折叠', N'宿舍阳台晾衣服用的，双杆设计挂得多。不锈钢材质不会生锈，底下带万向轮可以推着走。毕业带不走。', 25.00, 79.00, 3, N'daily',
 N'["https://picsum.photos/seed/clothes-rack-1/400/400","https://picsum.photos/seed/clothes-rack-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林4号楼"}', N'["晾衣架","宿舍","生活"]', 5, 2, 0, 0, 1, 2, 87, DATEADD(DAY, 30, DATEADD(DAY, -12, GETDATE())), DATEADD(DAY, -12, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230037'), N'全身镜 落地穿衣镜 40x150cm', N'贴在衣柜门上的那种，不会占地方。女生宿舍必备！边角有一点磕碰但不影响使用。', 20.00, 59.00, 3, N'daily',
 N'["https://picsum.photos/seed/mirror-1/400/400","https://picsum.photos/seed/mirror-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["镜子","穿衣镜","宿舍"]', 8, 3, 1, 0, 1, 2, 123, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230038'), N'简易鞋架 4层可放12双鞋', N'组装式鞋架，金属框架+无纺布层板。可以放12双鞋，占地面积小。用了半年，很稳固。', 15.00, 49.00, 3, N'daily',
 N'["https://picsum.photos/seed/shoe-rack-1/400/400","https://picsum.photos/seed/shoe-rack-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区20号楼"}', N'["鞋架","宿舍","收纳"]', 4, 1, 0, 0, 1, 2, 67, DATEADD(DAY, 30, DATEADD(DAY, -15, GETDATE())), DATEADD(DAY, -15, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230039'), N'懒人沙发 豆袋 单人位 灰色', N'坐下去就不想起来的那种舒服...填充物是EPP颗粒，久坐不塌。灰色耐脏，布套可以拆洗。租的房子用不上了。', 60.00, 239.00, 3, N'daily',
 N'["https://picsum.photos/seed/bean-bag-1/400/400","https://picsum.photos/seed/bean-bag-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区5号楼"}', N'["懒人沙发","宿舍","舒适"]', 18, 9, 1, 0, 1, 2, 312, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230040'), N'床帘 遮光款 90x190cm 上下铺通用', N'宿舍床帘，遮光效果一流！拉上了就是自己的小天地。深蓝色，带蚊帐层，夏天防蚊冬天保暖。含支架。', 35.00, 99.00, 3, N'daily',
 N'["https://picsum.photos/seed/bed-curtain-1/400/400","https://picsum.photos/seed/bed-curtain-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区14号楼"}', N'["床帘","遮光","宿舍"]', 10, 5, 0, 0, 1, 2, 167, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230041'), N'膳魔师保温杯 500ml 不锈钢 黑色', N'正品膳魔师，保温效果超强，早上倒的热水到晚上还是烫的。杯身有轻微使用痕迹，杯盖密封圈完好。', 50.00, 199.00, 3, N'daily',
 N'["https://picsum.photos/seed/thermos-1/400/400","https://picsum.photos/seed/thermos-2/400/400"]',
 N'', N'', N'{"id":"classroom","name":"教学楼","building":"东教学楼"}', N'["保温杯","膳魔师","日用品"]', 14, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230042'), N'双层饭盒 304不锈钢 带保温包', N'带饭去图书馆必备！304不锈钢双层，一层装饭一层装菜。自带保温包保温3小时左右。只用了几次。', 25.00, 69.00, 2, N'daily',
 N'["https://picsum.photos/seed/lunchbox-1/400/400","https://picsum.photos/seed/lunchbox-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆一楼"}', N'["饭盒","不锈钢","餐具"]', 5, 2, 1, 0, 1, 2, 89, DATEADD(DAY, 30, DATEADD(DAY, -11, GETDATE())), DATEADD(DAY, -11, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230043'), N'记忆棉坐垫 40x40cm 久坐不累', N'图书馆椅子太硬必备坐垫。记忆棉慢回弹，坐了半年没有塌。屁股总算不疼了。送一个同款腰靠。', 30.00, 89.00, 3, N'daily',
 N'["https://picsum.photos/seed/cushion-1/400/400","https://picsum.photos/seed/cushion-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆自习区"}', N'["坐垫","学习","舒适"]', 12, 6, 1, 0, 1, 2, 198, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230044'), N'公牛 USB排插 3口USB+4个插座', N'桌面充电站，3个USB口+4个5孔插座，桌面整洁多了。线长1.8米，公牛品质安全放心。用了8个月。', 35.00, 85.00, 3, N'daily',
 N'["https://picsum.photos/seed/power-strip-1/400/400","https://picsum.photos/seed/power-strip-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区7舍"}', N'["排插","公牛","充电"]', 16, 8, 0, 0, 1, 2, 267, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230045'), N'桌面收纳盒 5格 带抽屉', N'书桌整理神器！5个分区+2个小抽屉，笔、充电线、便签、耳机都有地方放了。木质纹路很ins风。', 15.00, 45.00, 2, N'daily',
 N'["https://picsum.photos/seed/desk-organizer-1/400/400","https://picsum.photos/seed/desk-organizer-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区22号楼"}', N'["收纳","桌面","整理"]', 9, 4, 1, 0, 1, 2, 145, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

-- ============================================
-- 衣物鞋帽 (10 items: #58-#67)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230001'), N'优衣库 轻薄羽绒服 男款 L号 黑色', N'优衣库超轻羽绒系列，非常轻薄但保暖效果很好。L码175/96A，穿了不到一冬天。买小了所以出，适合175左右的男生。', 120.00, 399.00, 2, N'clothing',
 N'["https://picsum.photos/seed/down-jacket-1/400/400","https://picsum.photos/seed/down-jacket-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林2号楼"}', N'["优衣库","羽绒服","男装"]', 12, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230003'), N'ZARA 春季风衣 女款 M码 卡其色', N'去年春天买的，穿过大概5次。M码适合160-165的女生，版型很好很显瘦。我胖了穿不下了😭。', 150.00, 499.00, 2, N'clothing',
 N'["https://picsum.photos/seed/trench-coat-1/400/400","https://picsum.photos/seed/trench-coat-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林5号楼"}', N'["ZARA","风衣","女装","春装"]', 18, 9, 1, 0, 1, 2, 367, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230004'), N'NIKE Air Force 1 空军一号 42码', N'经典AF1全白，百搭神器。穿了一个学期，鞋底正常磨损，鞋面很干净（定期擦）。鞋盒还在。换了新鞋所以出。', 280.00, 799.00, 3, N'clothing',
 N'["https://picsum.photos/seed/nike-af1-1/400/400","https://picsum.photos/seed/nike-af1-2/400/400","https://picsum.photos/seed/nike-af1-3/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"体育馆门口"}', N'["耐克","AF1","男鞋","经典"]', 28, 15, 1, 0, 1, 2, 523, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230006'), N'Vans Old Skool 经典款 37码 黑白', N'Vans经典款，百搭。37码适合平时穿36.5-37的女生。鞋底正常磨损，鞋面有轻微穿着痕迹。买小了半码所以出。', 120.00, 469.00, 3, N'clothing',
 N'["https://picsum.photos/seed/vans-1/400/400","https://picsum.photos/seed/vans-2/400/400"]',
 N'', N'', N'{"id":"classroom","name":"教学楼","building":"西教学楼"}', N'["Vans","帆布鞋","女鞋","经典"]', 15, 8, 1, 0, 1, 2, 289, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230020'), N'匡威 Chuck 70 高帮帆布鞋 41码', N'经典款1970s，米白色。穿了大概十次，鞋底花纹都还很清楚。买了两双换着穿，这双穿得少。', 180.00, 569.00, 2, N'clothing',
 N'["https://picsum.photos/seed/converse-1/400/400","https://picsum.photos/seed/converse-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["匡威","帆布鞋","男鞋","1970s"]', 20, 10, 1, 0, 1, 2, 398, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230023'), N'UR 连衣裙 S码 碎花 春夏款', N'UR的碎花连衣裙，今年超火的款式。S码适合90-100斤的女生。只穿了一次（拍照），吊牌还在。买了两条换着穿。', 80.00, 259.00, 1, N'clothing',
 N'["https://picsum.photos/seed/dress-floral-1/400/400","https://picsum.photos/seed/dress-floral-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区13号楼"}', N'["连衣裙","UR","女装","碎花"]', 22, 12, 1, 0, 1, 2, 456, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230024'), N'MLB棒球帽 卡其色 男女通用', N'MLB正品老花款，卡其色百搭。戴了一个夏天，帽檐没有变形，整体很新。头围可调节。', 80.00, 269.00, 2, N'clothing',
 N'["https://picsum.photos/seed/mlb-cap-1/400/400","https://picsum.photos/seed/mlb-cap-2/400/400"]',
 N'', N'', N'{"id":"canteen","name":"食堂门口","building":"柳林校区食堂"}', N'["MLB","棒球帽","潮牌"]', 10, 5, 0, 0, 1, 2, 176, DATEADD(DAY, 30, DATEADD(DAY, -7, GETDATE())), DATEADD(DAY, -7, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230026'), N'鸿星尔克 跑步鞋 男款 43码 黑色', N'国货之光！穿着跑了大概100公里，鞋底有正常磨损但缓震依然很好。43码适合平时42.5-43的男生。', 50.00, 299.00, 3, N'clothing',
 N'["https://picsum.photos/seed/running-shoe-1/400/400","https://picsum.photos/seed/running-shoe-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"操场入口"}', N'["跑鞋","鸿星尔克","男鞋","运动"]', 6, 3, 1, 0, 1, 2, 112, DATEADD(DAY, 30, DATEADD(DAY, -9, GETDATE())), DATEADD(DAY, -9, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230031'), N'H&M 牛仔外套 女款 M码 浅蓝色', N'经典牛仔外套，浅蓝色好看百搭。M码适合160-168女生，面料有弹性。穿了一季，袖口有轻微磨损（做旧款看不出来）。', 65.00, 249.00, 3, N'clothing',
 N'["https://picsum.photos/seed/denim-jacket-1/400/400","https://picsum.photos/seed/denim-jacket-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区9号楼"}', N'["牛仔外套","H&M","女装"]', 8, 4, 1, 0, 1, 2, 145, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230040'), N'北面 双肩包 30L 黑色 防水', N'北面经典款双肩包，30L容量装15.6寸笔记本绰绰有余。防水面料，下雨天背也不怕。用了两年，拉链顺滑，没有破损。换了新包所以出。', 120.00, 598.00, 3, N'clothing',
 N'["https://picsum.photos/seed/backpack-1/400/400","https://picsum.photos/seed/backpack-2/400/400","https://picsum.photos/seed/backpack-3/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆门口"}', N'["北面","双肩包","防水","户外"]', 16, 8, 1, 0, 1, 2, 312, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

-- ============================================
-- 运动器材 (10 items: #68-#77)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230004'), N'斯伯丁篮球 标准7号 PU材质', N'斯伯丁正品，室外场用了不到10次，纹路还很深。标准7号球（男生用），PU材质手感好。充气针和网袋都在。', 55.00, 199.00, 2, N'sports',
 N'["https://picsum.photos/seed/basketball-1/400/400","https://picsum.photos/seed/basketball-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"篮球场"}', N'["篮球","斯伯丁","运动"]', 12, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230006'), N'瑜伽垫 加厚10mm 防滑 TPE材质', N'TPE环保材质无异味，加厚10mm膝盖不疼。双面防滑纹理，做平板支撑也不滑。买了就去健身房了，这个闲置了。送收纳绑带。', 30.00, 89.00, 2, N'sports',
 N'["https://picsum.photos/seed/yoga-mat-1/400/400","https://picsum.photos/seed/yoga-mat-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"瑜伽室"}', N'["瑜伽垫","健身","运动"]', 8, 4, 1, 0, 1, 2, 145, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230016'), N'迪卡侬 哑铃套装 2x5kg 可拆卸', N'迪卡侬哑铃，一对共10kg（5kgx2），可以拆卸调节重量。用来练手臂和肩膀，用了三个月。换更重的了所以出。', 50.00, 149.00, 3, N'sports',
 N'["https://picsum.photos/seed/dumbbell-1/400/400","https://picsum.photos/seed/dumbbell-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"健身房"}', N'["哑铃","迪卡侬","健身"]', 15, 7, 0, 0, 1, 2, 267, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230018'), N'尤尼克斯羽毛球拍 入门级 两只', N'Yonex入门级球拍，两只一起出（可以和室友打）。铝合金材质很轻，磅数合适。送6个羽毛球和手胶。', 60.00, 198.00, 3, N'sports',
 N'["https://picsum.photos/seed/badminton-1/400/400","https://picsum.photos/seed/badminton-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"羽毛球馆"}', N'["羽毛球","尤尼克斯","运动"]', 10, 5, 1, 0, 1, 2, 189, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230022'), N'轮滑鞋 37-40可调 成人入门款', N'大一参加轮滑社买的，37-40码可调节。鞋面轻微磨损，轮子顺滑，刹车块还有一半。送护具三件套。后来没时间滑了。', 55.00, 199.00, 3, N'sports',
 N'["https://picsum.photos/seed/rollerskate-1/400/400","https://picsum.photos/seed/rollerskate-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"广场"}', N'["轮滑","运动","社团"]', 7, 3, 1, 0, 1, 2, 123, DATEADD(DAY, 30, DATEADD(DAY, -10, GETDATE())), DATEADD(DAY, -10, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230025'), N'迪卡侬 仰卧起坐辅助器 吸盘式', N'宿舍练腹肌神器！吸盘式固定在地上很稳，不会移动。用了两个月练出了四块腹肌（然后放弃了...）。', 20.00, 59.00, 3, N'sports',
 N'["https://picsum.photos/seed/situp-device-1/400/400","https://picsum.photos/seed/situp-device-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区6号楼"}', N'["健身","腹肌","宿舍"]', 6, 3, 0, 0, 1, 2, 98, DATEADD(DAY, 30, DATEADD(DAY, -8, GETDATE())), DATEADD(DAY, -8, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230028'), N'小米筋膜枪 按摩枪 6档调节', N'运动完放松肌肉的神器！6档力度可调，4个按摩头。用了半年，续航约6小时。换了Hyperice的所以出。', 80.00, 299.00, 2, N'sports',
 N'["https://picsum.photos/seed/fascia-gun-1/400/400","https://picsum.photos/seed/fascia-gun-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"健身房"}', N'["筋膜枪","按摩","运动恢复"]', 14, 7, 1, 0, 1, 2, 256, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230030'), N'自行车 凤凰牌 26寸 7档变速', N'凤凰26寸通勤车，7档变速应对学校各种坡。骑了不到一年，链条和变速器都正常。送车锁和后座坐垫。换了电动车所以出。', 280.00, 799.00, 3, N'sports',
 N'["https://picsum.photos/seed/bicycle-1/400/400","https://picsum.photos/seed/bicycle-2/400/400","https://picsum.photos/seed/bicycle-3/400/400"]',
 N'', N'', N'{"id":"canteen","name":"食堂门口","building":"食堂门口停车区"}', N'["自行车","凤凰","通勤"]', 25, 14, 1, 0, 1, 2, 512, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230035'), N'李宁 跳绳 竞速款 钢丝绳 计数', N'减肥跳绳，钢丝绳很轻快，带计数器可以记录次数。绳长约2.8米可以自己调。跳了不到一个月...', 15.00, 49.00, 2, N'sports',
 N'["https://picsum.photos/seed/jump-rope-1/400/400","https://picsum.photos/seed/jump-rope-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"操场"}', N'["跳绳","李宁","健身"]', 5, 2, 0, 0, 1, 2, 78, DATEADD(DAY, 30, DATEADD(DAY, -12, GETDATE())), DATEADD(DAY, -12, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230042'), N'乒乓球拍 红双喜 五星 横拍', N'红双喜五星级球拍，正手狂飙3反手天极3。适合有一定基础的球友。用了半年，胶皮还有8成新。', 60.00, 199.00, 3, N'sports',
 N'["https://picsum.photos/seed/pingpong-1/400/400","https://picsum.photos/seed/pingpong-2/400/400"]',
 N'', N'', N'{"id":"gym","name":"体育馆","building":"乒乓球室"}', N'["乒乓球","红双喜","运动"]', 8, 4, 1, 0, 1, 2, 134, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

-- ============================================
-- 美妆护肤 (5 items: #78-#82)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230007'), N'MAC口红 Chili色号 仅试用一次', N'MAC经典小辣椒色Chili，专柜价170。颜色不太适合我的肤色，只在手上试了一次。包装完整带盒子。', 70.00, 170.00, 1, N'beauty',
 N'["https://picsum.photos/seed/mac-lipstick-1/400/400","https://picsum.photos/seed/mac-lipstick-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林6号楼"}', N'["MAC","口红","美妆"]', 18, 10, 1, 0, 1, 2, 345, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230012'), N'完美日记 眼影盘 小鹿盘 12色', N'完美日记探险家系列小鹿盘，配色超美！用了大概5次，有几个颜色只是试色。粉质细腻好晕染。眼影盘太多用不过来。', 45.00, 129.00, 2, N'beauty',
 N'["https://picsum.photos/seed/eyeshadow-1/400/400","https://picsum.photos/seed/eyeshadow-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区15号楼"}', N'["眼影","完美日记","美妆"]', 12, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230026'), N'电动修眉刀 充电款 LED补光', N'充电式电动修眉刀，带LED补光灯，修眉的时候看得很清楚。刀头可以换，送了2个替换刀头。用了两次就不用了（手残...）。', 25.00, 79.00, 2, N'beauty',
 N'["https://picsum.photos/seed/eyebrow-trimmer-1/400/400","https://picsum.photos/seed/eyebrow-trimmer-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区9号楼"}', N'["修眉刀","美容工具"]', 5, 2, 1, 0, 1, 2, 89, DATEADD(DAY, 30, DATEADD(DAY, -9, GETDATE())), DATEADD(DAY, -9, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230033'), N'兰蔻 粉水 400ml 保湿化妆水', N'干皮救星！兰蔻粉水400ml大瓶装。用了大概1/4，泵头完好。保湿效果很好，但我换了更适合油皮的水。', 120.00, 420.00, 3, N'beauty',
 N'["https://picsum.photos/seed/lancome-toner-1/400/400","https://picsum.photos/seed/lancome-toner-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["兰蔻","化妆水","护肤"]', 16, 8, 1, 0, 1, 2, 312, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230040'), N'珀莱雅 双抗精华 30ml 全新未拆', N'珀莱雅双抗精华，抗氧化+抗糖化。双十一囤多了用不完，全新未拆封。保质期到2026年6月。', 85.00, 239.00, 1, N'beauty',
 N'["https://picsum.photos/seed/proya-serum-1/400/400","https://picsum.photos/seed/proya-serum-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区19号楼"}', N'["精华","珀莱雅","护肤"]', 20, 12, 0, 0, 1, 2, 398, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

-- ============================================
-- 乐器 (5 items: #83-#87)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230008'), N'雅马哈 F310 民谣吉他 41寸 含琴包', N'Yamaha F310，新手入门神器！学了半年会弹唱几首歌就没怎么弹了。琴弦刚换不久，琴颈笔直无变形。送调音器、变调夹和备用琴弦。', 380.00, 899.00, 2, N'instrument',
 N'["https://picsum.photos/seed/guitar-1/400/400","https://picsum.photos/seed/guitar-2/400/400","https://picsum.photos/seed/guitar-3/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区11舍"}', N'["吉他","雅马哈","乐器"]', 30, 18, 1, 0, 1, 2, 623, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230021'), N'卡林巴 拇指琴 17音 桃花心木', N'声音空灵好听，上手超简单，看谱子就能弹。桃花心木材质，音色温润。玩了不到一个月就放着了，基本全新。', 45.00, 129.00, 2, N'instrument',
 N'["https://picsum.photos/seed/kalimba-1/400/400","https://picsum.photos/seed/kalimba-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区21号楼"}', N'["拇指琴","卡林巴","乐器"]', 10, 5, 1, 0, 1, 2, 178, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230029'), N'电子琴 CASIO CTK-2550 61键', N'卡西欧入门电子琴，61键标准力度键盘。内置400种音色100种节奏，自带教学功能。想学钢琴练了两个月手指太僵硬放弃了...', 350.00, 899.00, 2, N'instrument',
 N'["https://picsum.photos/seed/keyboard-1/400/400","https://picsum.photos/seed/keyboard-2/400/400","https://picsum.photos/seed/keyboard-3/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区15号楼"}', N'["电子琴","卡西欧","乐器"]', 15, 8, 1, 0, 1, 2, 312, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230036'), N'尤克里里 enya 23寸 桃花心木', N'Enya尤克里里，23寸适合新手，弦距低按着手不疼。学了两首歌就吃灰了...琴包+调音器+备用琴弦都有。', 120.00, 369.00, 2, N'instrument',
 N'["https://picsum.photos/seed/ukulele-1/400/400","https://picsum.photos/seed/ukulele-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区9号楼"}', N'["尤克里里","enya","乐器"]', 12, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230045'), N'口琴 天鹅24孔复音 不锈钢盖板', N'天鹅牌24孔C调复音口琴，不锈钢盖板不易生锈。音色明亮，适合入门。学了半个月气息都不会用...送清洁布和教程。', 25.00, 69.00, 2, N'instrument',
 N'["https://picsum.photos/seed/harmonica-1/400/400","https://picsum.photos/seed/harmonica-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区6号楼"}', N'["口琴","天鹅","乐器"]', 4, 2, 0, 0, 1, 2, 67, DATEADD(DAY, 30, DATEADD(DAY, -11, GETDATE())), DATEADD(DAY, -11, GETDATE())),

-- ============================================
-- 数码配件 (10 items: #88-#97)
-- ============================================
((SELECT id FROM users WHERE student_id = N'20230002'), N'手机三脚架 蓝牙遥控 1.6米', N'拍Vlog和合照必备！高度可调最高1.6米，蓝牙遥控器10米范围内可以远程拍照。手机夹可以旋转横竖。用了两次闲置了。', 25.00, 79.00, 2, N'digital',
 N'["https://picsum.photos/seed/tripod-1/400/400","https://picsum.photos/seed/tripod-2/400/400"]',
 N'', N'', N'{"id":"canteen","name":"食堂门口","building":"校园广场"}', N'["三脚架","拍照","Vlog"]', 8, 4, 1, 0, 1, 2, 134, DATEADD(DAY, 30, DATEADD(DAY, -7, GETDATE())), DATEADD(DAY, -7, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230009'), N'Anker PD快充头 65W GaN氮化镓', N'Anker 65W GaN充电头，体积小巧功率大。可以给笔记本（支持PD）、平板、手机同时充。用了半年，换了更大功率的所以出。', 80.00, 199.00, 2, N'digital',
 N'["https://picsum.photos/seed/charger-1/400/400","https://picsum.photos/seed/charger-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["充电器","Anker","GaN","快充"]', 22, 12, 0, 0, 1, 2, 389, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230014'), N'绿联 Type-C扩展坞 7合1', N'MacBook/iPad必备扩展坞。HDMI+USB3.0x3+SD/TF读卡器+PD充电口。用了不到半年，所有接口正常。换了雷电扩展坞所以出。', 90.00, 259.00, 2, N'digital',
 N'["https://picsum.photos/seed/dock-1/400/400","https://picsum.photos/seed/dock-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆自习区"}', N'["扩展坞","Type-C","绿联","笔记本"]', 16, 8, 0, 0, 1, 2, 298, DATEADD(DAY, 30, DATEADD(DAY, -3, GETDATE())), DATEADD(DAY, -3, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230017'), N'闪迪 128G U盘 USB3.1 高速', N'闪迪CZ73酷铄金属U盘，128G容量。USB3.1传输速度读150MB/s，做启动盘或者存资料都很好用。', 40.00, 99.00, 2, N'digital',
 N'["https://picsum.photos/seed/usb-drive-1/400/400","https://picsum.photos/seed/usb-drive-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆电子阅览室"}', N'["U盘","闪迪","存储"]', 10, 5, 0, 0, 1, 2, 178, DATEADD(DAY, 30, DATEADD(DAY, -8, GETDATE())), DATEADD(DAY, -8, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230019'), N'手机散热器 半导体制冷 磁吸款', N'打原神/吃鸡必备！半导体制冷效果超强，手机贴着的地方能到10度以下。磁吸款（iPhone 12以上直接吸，安卓需要贴磁吸片）。', 30.00, 89.00, 2, N'digital',
 N'["https://picsum.photos/seed/phone-cooler-1/400/400","https://picsum.photos/seed/phone-cooler-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区12号楼"}', N'["散热器","手机配件","游戏"]', 10, 5, 1, 0, 1, 2, 167, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230023'), N'小米无线充电器 20W Max 白色', N'小米无线充电板，Qi协议通用。iPhone最高7.5W，安卓最高20W。放在桌面上随放随充很方便。换了MagSafe所以出。', 35.00, 99.00, 2, N'digital',
 N'["https://picsum.photos/seed/wireless-charger-1/400/400","https://picsum.photos/seed/wireless-charger-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区17号楼"}', N'["无线充电","小米","手机配件"]', 12, 6, 1, 0, 1, 2, 198, DATEADD(DAY, 30, DATEADD(DAY, -5, GETDATE())), DATEADD(DAY, -5, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230027'), N'金士顿 500G 移动固态硬盘 USB3.2', N'金士顿XS2000，500GB，USB3.2 Gen2x2速度最高2000MB/s。用来备份资料和装游戏，用了半年。换了1TB的所以出。', 200.00, 499.00, 2, N'digital',
 N'["https://picsum.photos/seed/ssd-1/400/400","https://picsum.photos/seed/ssd-2/400/400"]',
 N'', N'', N'{"id":"lib","name":"图书馆","building":"图书馆电子阅览室"}', N'["硬盘","移动固态","金士顿","存储"]', 18, 9, 0, 0, 1, 2, 345, DATEADD(DAY, 30, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230032'), N'手机直播补光灯 环形 三色温', N'拍视频/直播补光用的，三种色温可调，10档亮度。手机夹在中间拍出来皮肤超好。用了两个月闲置了。', 35.00, 99.00, 2, N'digital',
 N'["https://picsum.photos/seed/ring-light-1/400/400","https://picsum.photos/seed/ring-light-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区6号楼"}', N'["补光灯","直播","拍照"]', 7, 3, 1, 0, 1, 2, 123, DATEADD(DAY, 30, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230037'), N'AirTag 防丢器 单只装 全新未拆', N'买手机送的赠品，全新未拆封。挂在钥匙或者书包上，找不到的时候用iPhone精确查找。', 120.00, 249.00, 1, N'digital',
 N'["https://picsum.photos/seed/airtag-1/400/400","https://picsum.photos/seed/airtag-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区11号楼"}', N'["AirTag","苹果","防丢","全新"]', 15, 8, 0, 0, 1, 2, 267, DATEADD(DAY, 30, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230044'), N'手机镜头 广角+微距 二合一 夹式', N'手机摄影神器！夹在手机摄像头上就能用，广角镜头拍风景，微距镜头拍花虫。效果不错，换了相机所以出。', 30.00, 89.00, 2, N'digital',
 N'["https://picsum.photos/seed/phone-lens-1/400/400","https://picsum.photos/seed/phone-lens-2/400/400"]',
 N'', N'', N'{"id":"canteen","name":"食堂门口","building":"校园广场"}', N'["手机镜头","摄影","配件"]', 6, 3, 1, 0, 1, 2, 98, DATEADD(DAY, 30, DATEADD(DAY, -9, GETDATE())), DATEADD(DAY, -9, GETDATE())),

-- ============================================
-- 其他 (5 items: #98-#102)
-- ============================================
((SELECT id FROM users WHERE student_id = N'admin'), N'猫砂盆 半封闭式 带铲子 粉色', N'养猫人必备！半封闭式设计防臭防溅出。以前养猫的时候用的，现在猫送回家养了。已经消毒清洗干净。附送猫砂铲。', 20.00, 59.00, 3, N'other',
 N'["https://picsum.photos/seed/cat-litter-box-1/400/400","https://picsum.photos/seed/cat-litter-box-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林1号楼"}', N'["猫砂盆","宠物","猫"]', 5, 3, 1, 0, 1, 2, 89, DATEADD(DAY, 30, DATEADD(DAY, -13, GETDATE())), DATEADD(DAY, -13, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20240001'), N'北极熊玩偶 1.5米 超大号 白色', N'生日礼物收到的，太大了床上放不下😂。1.5米大号北极熊，超软超好抱。基本全新就在角落放了半年。', 40.00, 159.00, 2, N'other',
 N'["https://picsum.photos/seed/bear-plush-1/400/400","https://picsum.photos/seed/bear-plush-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林5号楼"}', N'["玩偶","礼物","北极熊"]', 12, 6, 1, 0, 1, 2, 234, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230013'), N'重庆火锅底料 德庄 1kg 未开封', N'本来想在宿舍煮火锅用的（违规操作请勿模仿），结果买回来一直没机会。德庄特辣底料，适合3-4人份。保质期到2027年3月。', 15.00, 39.90, 1, N'other',
 N'["https://picsum.photos/seed/hotpot-sauce-1/400/400","https://picsum.photos/seed/hotpot-sauce-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区16号楼"}', N'["火锅","底料","美食"]', 8, 4, 0, 0, 1, 2, 156, DATEADD(DAY, 30, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230034'), N'复古煤油灯 装饰品 送煤油', N'很文艺的复古煤油灯，放在书架或者窗台上都很好看。买来当装饰的，没有实际点过。送一小瓶煤油。', 25.00, 69.00, 1, N'other',
 N'["https://picsum.photos/seed/oil-lamp-1/400/400","https://picsum.photos/seed/oil-lamp-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区10号楼"}', N'["装饰","复古","文艺"]', 6, 2, 1, 0, 1, 2, 98, DATEADD(DAY, 30, DATEADD(DAY, -7, GETDATE())), DATEADD(DAY, -7, GETDATE())),

((SELECT id FROM users WHERE student_id = N'20230039'), N'宿舍门牌 木质定制 可写字', N'手工木质门牌，可以用粉笔写房间号和留言。挂在宿舍门上可可爱爱！全新没用过（室友说太幼稚...）。', 10.00, 29.00, 1, N'other',
 N'["https://picsum.photos/seed/door-sign-1/400/400","https://picsum.photos/seed/door-sign-2/400/400"]',
 N'', N'', N'{"id":"dorm","name":"宿舍楼下","building":"柳林校区18号楼"}', N'["门牌","宿舍","手工","装饰"]', 4, 2, 0, 0, 1, 2, 56, DATEADD(DAY, 30, DATEADD(DAY, -10, GETDATE())), DATEADD(DAY, -10, GETDATE()));

    PRINT N'100 goods inserted successfully.';
END
ELSE
BEGIN
    PRINT N'100 goods already exist, skipping.';
END

PRINT N'All seed data (50 users + 100 goods) processed successfully.';
