
-- �û���
-- drop table if exists tb_user;
-- �˺ţ����룬�û�����ͷ���Ա����䣬��ȡ�򣬻���״�������ڵأ�ע��ʱ�䣬��ע���������ղص����ӡ�
create table tb_user(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username varchar(50) NOT NULL,
	password varchar(30) NOT NULL,
	nickname varchar(50),		-- �ǳ�
	head_picture varchar(100),	-- �û�ͷ��
	sex tinyint,			-- �Ա�1-�У�2-Ů
	sex_switch bit(1),		-- ���أ����ܡ�����
	age tinyint,			-- ����
	age_switch bit(1),		-- ���أ����ܡ�����
	sex_orient tinyint,			-- ��ȡ��1-���У�2-��Ů��3-˫ɱ��4-������
	sex_orient_switch bit(1),	-- ���أ����ܡ�����
	marital_status tinyint,			-- ���������1-δ�飬2-�ѻ�
	marital_status_switch bit(1),	-- ���أ����ܡ�����
	location varchar(25),		-- ���ڵ�
	create_time datetime NOT NULL,
	update_time datetime,
	latest_login_time datetime,			-- �����¼ʱ��
	enabled bit(1) NOT NULL DEFAULT 1,	-- 0�������ã�1������
	deleted bit(1) NOT NULL DEFAULT 1	-- 0��������1:ɾ��
);
-- ��������
CREATE INDEX i_user_username ON tb_user (username);





-- ��������
-- drop table if exists tb_community_category;
create table tb_community_category(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name varchar(25),		-- ��������
	descr varchar(100),		-- ����
	parent_id int,			-- ������ID
	user_interest_cnt int	-- �û���ע����
);


-- �û��ղر�
-- drop table if exists tb_user_note_collect;
create table tb_user_note_collect(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- �û�ID
	note_id bigint(20),	-- ����ID
	category_id int(11),-- �������ࣨ���������ҵ��������������ӱ��У�
	create_time datetime NOT NULL
);

-- �û���ע���������
-- drop table if exists tb_user_category_interest;
create table tb_user_category_interest(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- �û�ID
	category_id int(11)	-- ����ID
);


-- ���ӱ�,������������ʱ�ֱ���������ID���෨��100�ű�
-- drop table if exists tb_community_note;
create table tb_community_note(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	title varchar(255),		-- ����
	content text,			-- ����
	pictures varchar(255),	-- ͼƬ,���ŷָ�
	user_id int(11),		-- ������ID
	create_time datetime,   -- ����ʱ��
	category_id int(11),	-- ����ID
	deleted bit(1),			-- �Ƿ�ɾ��
	anonymous bit(1)		-- �Ƿ���������
	
);

-- ����ͳ�Ʊ�
-- drop table if exists tb_community_note_counter;
create table tb_community_note_counter(
	note_id bigint(20) primary key,  -- ����ID
	scan_count tinyint		-- �������
);
	
	
-- ���ޱ�
-- drop table if exists tb_good_click;
create table tb_good_click(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11), 	-- �û�ID
	object_id bigint(20), -- ���޶���ID
	good_type tinyint   -- ��ԣ�1-���ӣ�2-���� --����������󣬴��ֶ���ʵûʲô����
);


-- �������ۻظ���
-- drop table if exists tb_community_note_comment;
create table tb_community_note_comment(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	note_id bigint(20),		-- ����ID
	root_comment_id bigint(20),	-- ���۸�ID
	commented_id bigint(20),	-- ���ظ��ļ�¼ID
	content varchar(255),	-- ��������
	content_user_id int(11),-- ������ID
	create_time datetime,	-- ����ʱ��
	deleted bit(1) 			-- �Ƿ�ɾ��
);

-- ���Ӿٱ�Ͷ�߱�
-- drop table if exists tb_note_report;
create table tb_note_report(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	note_id bigint(20),		-- ����ID
	report_type tinyint,	-- �ٱ�����
	user_id int(11),		-- �ٱ���
	create_time datetime	-- �ٱ�ʱ��
);


-- ---------------- �����������ǻ���redis����ʱ�Ž����� ----------------------------

-- �û�����ʱ�������������������
-- drop table if exists tb_community_note_comment_index;
create table tb_community_note_comment_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL, -- noteId������ID
	indexType tinyint NOT NULL, -- 1-����ID��������ID,2-����ID�����ظ�ID
	commentId varchar(30) NOT NULL, -- ����ID���ظ�ID
	subScore bigint(20),  -- ���
	createTime datetime   -- subScore is nullʱ������createTime�������
);

-- �û�����ʱ���������������tb_user_note_index Ҫ�ֱ�����indexId hash �ֱ�
-- drop table if exists tb_community_note_index;
create table tb_community_note_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL,
	indexType tinyint NOT NULL, -- �����û�id�������ӣ�user2Note(1),���ݷ���id�������ӣ�category2Note(2)
	noteId varchar(30) NOT NULL,
	subScore bigint(20),  -- ���
	createTime datetime   -- subScore is nullʱ������createTime�������
);

-- �û�����ʱ����ӵ���������
-- drop table if exists tb_good_click_index;
create table tb_good_click_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL, -- ����ID������ID
	indexType tinyint NOT NULL, -- 1-���ӣ�2-����
	goodId varchar(30) NOT NULL,-- ���ޱ�����ID
	subScore bigint(20),  		-- ���
	createTime datetime   		-- subScore is nullʱ������createTime�������
);

-- �û��ղ�������
-- drop table if exists tb_user_note_collect_index;
create table tb_user_note_collect_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL, -- ����ID���û�ID
	indexType tinyint NOT NULL, -- 1-�����������ղص��û���2-�û������Ѳص�����
	collectId varchar(30) NOT NULL,-- �ղر�����ID
	subScore bigint(20),  		-- ���
	createTime datetime   		-- subScore is nullʱ������createTime�������
);

-- ��������ID��ȡһ�������б���ҳ���������µĶ����ظ��б�





