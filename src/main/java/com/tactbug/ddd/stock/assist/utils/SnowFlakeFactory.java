package com.tactbug.ddd.stock.assist.utils;

public class SnowFlakeFactory {
    /**
     * ID中41位时间戳的起点 (2020-01-01 00:00:00.00)
     * @apiNote 一般地，选用系统上线的时间
     */
    private static final long startPoint = 1577808000000L;

    /**
     * 序列号位数
     */
    private static final long sequenceBits = 12L;

    /**
     * 机器ID位数
     */
    private static final long workerIdBits = 5L;

    /**
     * 数据中心ID位数
     */
    private static final long dataCenterIdBits = 5L;

    /**
     * 序列号最大值, 4095
     * @apiNote 4095 = 0xFFF,其相当于是序列号掩码
     */
    private final long sequenceMask = -1L^(-1L<<sequenceBits);

    /**
     * 机器ID最大值, 31
     */
    private final long maxWorkerId = -1L^(-1L<<workerIdBits);

    /**
     * 数据中心ID最大值, 31
     */
    private final long maxDataCenterId = -1L^(-1L<<dataCenterIdBits);

    /**
     * 机器ID左移位数, 12
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据中心ID左移位数, 12+5
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间戳左移位数, 12+5+5
     */
    private final long timeStampShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 数据中心ID, Value Range: [0,31]
     */
    private long dataCenterId;

    /**
     * 机器ID, Value Range: [0,31]
     */
    private long workerId;

    /**
     * 相同毫秒内的序列号, Value Range: [0,4095]
     */
    private long sequence = 0L;

    /**
     * 上一个生成ID的时间戳
     */
    private long lastTimeStamp = -1L;

    /**
     * 构造器
     * @param dataCenterId  数据中心ID
     * @param workerId 机器中心ID
     */
    public SnowFlakeFactory(Long dataCenterId, Long workerId) {
        if(dataCenterId==null || dataCenterId < 0 || dataCenterId>maxDataCenterId
                || workerId==null || workerId < 0 || workerId>maxWorkerId) {
            throw new IllegalArgumentException("输入参数错误");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 获取ID
     * @return
     */
    public synchronized long nextId() {
        long currentTimeStamp = System.currentTimeMillis();
        //当前时间小于上一次生成ID的时间戳，系统时钟被回拨
        if( currentTimeStamp < lastTimeStamp ) {
            throw new RuntimeException("系统时钟被回拨");
        }

        // 当前时间等于上一次生成ID的时间戳,则通过序列号来区分
        if( currentTimeStamp == lastTimeStamp ) {
            // 通过序列号掩码实现只取 (sequence+1) 的低12位结果，其余位全部清零
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0) { // 该时间戳下的序列号已经溢出
                // 阻塞等待下一个毫秒,并获取新的时间戳
                currentTimeStamp = getNextMs(lastTimeStamp);
            }
        } else {    // 当前时间大于上一次生成ID的时间戳,重置序列号
            sequence = 0;
        }

        // 更新上次时间戳信息
        lastTimeStamp = currentTimeStamp;

        // 生成此次ID
        long nextId = ((currentTimeStamp-startPoint) << timeStampShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;

        return nextId;
    }

    /**
     * 阻塞等待,直到获取新的时间戳(下一个毫秒)
     * @param lastTimeStamp
     * @return
     */
    private long getNextMs(long lastTimeStamp) {
        long timeStamp = System.currentTimeMillis();
        while(timeStamp<=lastTimeStamp) {
            timeStamp = System.currentTimeMillis();
        }
        return timeStamp;
    }
}
