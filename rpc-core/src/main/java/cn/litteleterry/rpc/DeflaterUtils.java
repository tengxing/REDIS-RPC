package cn.litteleterry.rpc;

import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * DeflaterUtils 压缩字符串
 */
public class DeflaterUtils {
    /**
     * 压缩
     */
    public static String zipString(String unzipString) {
        /*
         *     https://www.yiibai.com/javazip/javazip_deflater.html#article-start
         *     0 ~ 9 压缩等级 低到高
         *     public static final int BEST_COMPRESSION = 9;            最佳压缩的压缩级别。
         *     public static final int BEST_SPEED = 1;                  压缩级别最快的压缩。
         *     public static final int DEFAULT_COMPRESSION = -1;        默认压缩级别。
         *     public static final int DEFAULT_STRATEGY = 0;            默认压缩策略。
         *     public static final int DEFLATED = 8;                    压缩算法的压缩方法(目前唯一支持的压缩方法)。
         *     public static final int FILTERED = 1;                    压缩策略最适用于大部分数值较小且数据分布随机分布的数据。
         *     public static final int FULL_FLUSH = 3;                  压缩刷新模式，用于清除所有待处理的输出并重置拆卸器。
         *     public static final int HUFFMAN_ONLY = 2;                仅用于霍夫曼编码的压缩策略。
         *     public static final int NO_COMPRESSION = 0;              不压缩的压缩级别。
         *     public static final int NO_FLUSH = 0;                    用于实现最佳压缩结果的压缩刷新模式。
         *     public static final int SYNC_FLUSH = 2;                  用于清除所有未决输出的压缩刷新模式; 可能会降低某些压缩算法的压缩率。
         */

        //使用指定的压缩级别创建一个新的压缩器。
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        //设置压缩输入数据。
        deflater.setInput(unzipString.getBytes(StandardCharsets.UTF_8));
        //当被调用时，表示压缩应该以输入缓冲区的当前内容结束。
        deflater.finish();

        final byte[] bytes = new byte[512];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);

        while (!deflater.finished()) {
            //压缩输入数据并用压缩数据填充指定的缓冲区。
            int length = deflater.deflate(bytes);
            outputStream.write(bytes, 0, length);
        }
        //关闭压缩器并丢弃任何未处理的输入。
        deflater.end();
        return Base64.encodeBase64String(outputStream.toByteArray());
        //处理回车符
//        return zipString.replaceAll("[\r\n]", "");
    }

    /**
     * 解压缩
     */
    public static String unzipString(String zipString) {
        byte[] decode = Base64.decodeBase64(zipString);
        //创建一个新的解压缩器  https://www.yiibai.com/javazip/javazip_inflater.html

        Inflater inflater = new Inflater();
        //设置解压缩的输入数据。
        inflater.setInput(decode);
        final byte[] bytes = new byte[512];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        try {
            //finished() 如果已到达压缩数据流的末尾，则返回true。
            while (!inflater.finished()) {
                //将字节解压缩到指定的缓冲区中。
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭解压缩器并丢弃任何未处理的输入。
            inflater.end();
        }
        try {
            return outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String json = "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";
        String json2 = DeflaterUtils.unzipString(json);
        System.out.println("解压后:"+json2);
    }
}