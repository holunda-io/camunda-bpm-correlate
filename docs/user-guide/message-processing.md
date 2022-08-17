Messages are processed in batches triggered by a scheduler. There are following options to set-up the attributes of this scheduler:

... More details will follow

## Running in a cluster

For activation of the cluster support, please add the following configuration snippet to your `application.yml`:

```yaml
correlate:
  batch:
    cluster:
      enabled: true
      queuePollLockMostInterval: PT5M
```

For a cluster operations it is important to synchronize the batch schedulers between the cluster nodes. For this purpose, the library
[Shedlock](https://github.com/lukas-krecan/ShedLock) is used. Shedlock synchronizes the scheduled tasks using a RDBMS table. Here are
the required DDL snippets for some common RDBMSs.



```tsql 
CREATE TABLE shedlock
(
    name       NVARCHAR(64)  NOT NULL,
    lock_until DATETIME2     NOT NULL,
    locked_at  DATETIME2     NOT NULL,
    locked_by  NVARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);
```

```h2 
CREATE TABLE shedlock
(
    name       VARCHAR(64)  NOT NULL,
    lock_until DATETIME2     NOT NULL,
    locked_at  DATETIME2     NOT NULL,
    locked_by  VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);
```

