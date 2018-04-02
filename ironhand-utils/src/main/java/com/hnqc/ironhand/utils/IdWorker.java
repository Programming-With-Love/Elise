package com.hnqc.ironhand.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 分布式id生成器<br>
 * <p>twitter snowflake的java实现</p>
 *
 * @author zido
 */
public class IdWorker {

    private final static IdWorker instance;

    static {
        CommonConfig commonConfig = SpringUtils.getBean(CommonConfig.class);
        if (commonConfig != null && commonConfig.getDataCenterId() != null && commonConfig.getWorkerId() != null) {
            instance = new IdWorker(commonConfig.getWorkerId(), commonConfig.getDataCenterId());
        } else {
            instance = new IdWorker();
        }
    }

    public static long nextId() {
        return instance.next();
    }

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;
    /**
     * 数据标识id所占的位数
     */
    private final long dataCenterIdBits = 5L;
    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);
    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);

    private long workerId;
    private long dataCenterId;
    private long sequence;
    private long lastTimestamp = -1L;

    public IdWorker() {
        dataCenterId = getDataCenterId(maxDataCenterId);
        workerId = getMaxWorkerId(dataCenterId, maxWorkerId);
    }

    public IdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }

        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获取 maxWorkerId
     *
     * @param dataCenterId 数据中心id
     * @param maxWorkerId  机器id
     * @return maxWorkerId
     */
    private static long getMaxWorkerId(long dataCenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (name != null && "".equals(name)) {
            // GET jvmPid
            mpid.append(name.split("@")[0]);
        }
        //MAC + PID 的 hashcode 获取16个低位
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * <p>
     * 数据标识id
     * </p>
     */
    private static long getDataCenterId(long maxDataCenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDataCenterId + 1);
                }
            }
        } catch (Exception e) {
            System.err.println(" getDataCenterId: " + e.getMessage());
        }
        return id;
    }

    public synchronized long next() {
        long timeStamp = timeGen();
        if (timeStamp < lastTimestamp) {
            long offset = lastTimestamp - timeStamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timeStamp = timeGen();
                    if (timeStamp < lastTimestamp) {
                        throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //序列在id中占的位数
        long sequenceBits = 12L;
        if (lastTimestamp == timeStamp) {
            //生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
            long sequenceMask = ~(-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timeStamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timeStamp;
        //时间截向左移22位(5+5+12)
        long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
        //数据标识id向左移17位(12+5)
        long dataCenterIdShift = sequenceBits + workerIdBits;
        long twEpoch = 1288834974657L;
        return ((timeStamp - twEpoch)) << timestampLeftShift
                | (dataCenterId << dataCenterIdShift)
                | (workerId << sequenceBits)        //机器ID向左移12位
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }

        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return SystemClock.now();
    }

}
