package org.aminakov.cassandra.hadoop;

/**
 * @author Oleksandr Minakov
 *         date 9/22/13
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.hadoop.ColumnFamilyOutputFormat;

import org.apache.cassandra.db.IColumn;
import org.apache.cassandra.hadoop.ColumnFamilyInputFormat;
import org.apache.cassandra.hadoop.ConfigHelper;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCount extends Configured implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(WordCount.class);
    static final String KEYSPACE = "wordcount";
    static final String COLUMN_FAMILY = "inputs";

    static final String OUTPUT_REDUCER_VAR = "output_reducer";
    static final String OUTPUT_COLUMN_FAMILY = "output_words";

    private static final String OUTPUT_PATH_PREFIX = "/tmp/word_count";

    public static void main(String[] args) throws Exception
    {
        // Let ToolRunner handle generic command-line options
        ToolRunner.run(new Configuration(), new WordCount(), args);
        System.exit(0);
    }

    @Override
    public int run(String[] args) throws Exception {
        String outputReducerType = "filesystem";
        if (args != null && args[0].startsWith(OUTPUT_REDUCER_VAR))
        {
            String[] s = args[0].split("=");
            if (s != null && s.length == 2)
                outputReducerType = s[1];
        }
        logger.info("output reducer type: " + outputReducerType);

        Job job = new Job(getConf(), "wordcount");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(MapUsers.class);
        job.setReducerClass(ReduceUsers.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Map.class);
        job.setOutputValueClass(List.class);
        job.setOutputFormatClass(ColumnFamilyOutputFormat.class);

        ConfigHelper.setOutputColumnFamily(job.getConfiguration(), KEYSPACE, OUTPUT_COLUMN_FAMILY);
        job.getConfiguration().set(PRIMARY_KEY, "word,sum");
        String query = "UPDATE " + KEYSPACE + "." + OUTPUT_COLUMN_FAMILY +
                " SET count_num = ? ";
        CqlConfigHelper.setOutputCql(job.getConfiguration(), query);
        ConfigHelper.setOutputInitialAddress(job.getConfiguration(), "localhost");
        ConfigHelper.setOutputPartitioner(job.getConfiguration(), "Murmur3Partitioner");

        job.setInputFormatClass(CqlPagingInputFormat.class);

        ConfigHelper.setInputRpcPort(job.getConfiguration(), "9160");
        ConfigHelper.setInputInitialAddress(job.getConfiguration(), "localhost");
        ConfigHelper.setInputColumnFamily(job.getConfiguration(), KEYSPACE, COLUMN_FAMILY);
        ConfigHelper.setInputPartitioner(job.getConfiguration(), "Murmur3Partitioner");

        CqlConfigHelper.setInputCQLPageRowSize(job.getConfiguration(), "3");
        //this is the user defined filter clauses, you can comment it out if you want count all titles
        CqlConfigHelper.setInputWhereClauses(job.getConfiguration(), "title='A'");
        job.waitForCompletion(true);
        return 0;
    }
}
