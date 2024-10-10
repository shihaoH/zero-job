DROP TABLE IF EXISTS job_event;
CREATE TABLE job_event
(
    event_id           VARCHAR(32) NOT NULL COMMENT '事件 id',
    event_name         VARCHAR(32) COMMENT '事件名称',
    event_key          VARCHAR(32) COMMENT '事件 key',
    cron               VARCHAR(16) COMMENT '执行时间 cron 表达式，非立即执行情况下有效',
    job_params         text COMMENT '任务参数',
    job_params_type    ENUM ('STRING', 'INTEGER', 'NUMBER', 'BOOLEAN', 'ARRAY', 'OBJECT') COMMENT '任务参数类型',
    job_type           ENUM ('SCHEDULED', 'ONE_TIME', 'RECURRING') COMMENT '任务类型',
    job_count          INT NOT NULL DEFAULT -1 COMMENT '任务次数 -1表示无限制',
    executed_count     INT NOT NULL DEFAULT 0 COMMENT '已执行次数',
    start_time         DATETIME COMMENT '开始时间',
    end_time           DATETIME COMMENT '结束时间',
    job_executor       VARCHAR(255) COMMENT '任务执行者',
    job_execute_target VARCHAR(255) COMMENT '任务执行目标',
    create_time        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
    event_status       ENUM ('RUNNING', 'COMPLETED', 'CANCELED') NOT NULL DEFAULT 'RUNNING' COMMENT '事件状态',
    PRIMARY KEY (`event_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='任务事件表';

DROP TABLE IF EXISTS job;
CREATE TABLE job
(
    job_key            VARCHAR(255) NOT NULL COMMENT '任务唯一标识',
    job_name           VARCHAR(255) COMMENT '任务名称',
    job_group          VARCHAR(255) COMMENT '任务分组/批次',
    job_execute_time   DATETIME COMMENT '任务执行时间',
    job_executor       VARCHAR(255) COMMENT '任务执行者',
    job_execute_target VARCHAR(255) COMMENT '任务执行目标',
    job_params         text COMMENT '任务执行参数',
    job_params_type    ENUM ('STRING', 'INTEGER', 'NUMBER', 'BOOLEAN', 'ARRAY', 'OBJECT') COMMENT '任务参数类型',
    job_status         ENUM ('CANCELLED', 'RUNNING', 'SUCCEED', 'FAILED') NOT NULL DEFAULT 'RUNNING' COMMENT '任务状态',
    create_time        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
    PRIMARY KEY (`job_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='任务表';
