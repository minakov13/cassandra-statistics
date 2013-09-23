package org.aminakov.cassandra.hadoop;

import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleksandr Minakov
 *         date 9/23/13
 */
public class ReduceUsers extends Reducer<Text, IntWritable, Map<String, ByteBuffer>, List<ByteBuffer>> {
    private static final String PRIMARY_KEY = "row_key";
    private Map<String, ByteBuffer> keys;
    private ByteBuffer key;
    protected void setup(org.apache.hadoop.mapreduce.Reducer.Context context)
            throws IOException, InterruptedException
    {
        keys = new LinkedHashMap<String, ByteBuffer>();
        String[] partitionKeys = context.getConfiguration().get(PRIMARY_KEY).split(",");
        keys.put("row_id1", ByteBufferUtil.bytes(partitionKeys[0]));
        keys.put("row_id2", ByteBufferUtil.bytes(partitionKeys[1]));
    }

    public void reduce(Text word, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    {
        int sum = 0;
        for (IntWritable val : values)
            sum += val.get();
        context.write(keys, getBindVariables(word, sum));
    }

    private List<ByteBuffer> getBindVariables(Text word, int sum)
    {
        List<ByteBuffer> variables = new ArrayList<ByteBuffer>();
        keys.put("word", ByteBufferUtil.bytes(word.toString()));
        variables.add(ByteBufferUtil.bytes(String.valueOf(sum)));
        return variables;
    }
}
