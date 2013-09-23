package org.aminakov.cassandra.hadoop;

import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.Map;

/**
 * @author Oleksandr Minakov
 *         date 9/22/13
 */
public class MapUsers extends Mapper<Map<String, ByteBuffer>, Map<String, ByteBuffer>, Text, Text> {
    private static final Logger logger = LoggerFactory.getLogger(MapUsers.class);
    long sum = -1;
    public void map(Map<String, ByteBuffer> key, Map<String, ByteBuffer> columns, Context context) throws IOException, InterruptedException
    {
        if (sum < 0)
            sum = 0;

        logger.debug("read " + toString(key) + ":count_num from " + context.getInputSplit());
        sum += Long.valueOf(ByteBufferUtil.string(columns.get("count_num")));
    }

    protected void cleanup(Context context) throws IOException, InterruptedException {
        if (sum > 0)
            context.write(new Text("total_count "), new Text(String.valueOf(sum)));
    }

    private String toString(Map<String, ByteBuffer> keys)
    {
        String result = "";
        try
        {
            for (ByteBuffer key : keys.values())
                result = result + ByteBufferUtil.string(key) + ":";
        }
        catch (CharacterCodingException e)
        {
            logger.error("Failed to print keys", e);
        }
        return result;
    }
}
