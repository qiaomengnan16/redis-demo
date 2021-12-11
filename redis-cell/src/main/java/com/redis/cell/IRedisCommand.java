package com.redis.cell;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;
import java.util.List;

public interface IRedisCommand extends Commands {

    @Command("CL.THROTTLE ?0 ?1 ?2 ?3 ?4")
    List<Object> throttle(String key, long maxBurst, long tokens, long seconds, long apply);

}
