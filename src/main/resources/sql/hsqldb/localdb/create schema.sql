CREATE TABLE STATS_DIR_LOCAL_SIZE (
  NODE_ID bigint NOT NULL,
  PARENT_NODE_ID bigint,
  DIR_LOCAL_SIZE bigint,
  DIR_SUM_SIZE bigint,
  CONSTRAINT STATS_DIR_LOCAL_SIZE_PK PRIMARY KEY (NODE_ID)
)