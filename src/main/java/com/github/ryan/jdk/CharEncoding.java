package com.github.ryan.jdk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 字符编码:
 * 参考文章: https://www.jianshu.com/p/877481ae27cc
 * 1) 先知道字符集: 字符集就是所有字符构成的集合
 * 例如世界上所有使用的符号构成了一个字符集，这个字符集叫Unicode
 * Unicode为所有的字符都分配了一个编号（术语是code points）
 * 这个编号使用"U+<十六进制>"形式表示
 * 例如简体汉字“国”在Unicode的编号是”U+56FD"
 * Java本身就支持Unicode，Java 8支持的Unicode版本为6.2.0。
 * 在Java中使用两个字节来表示一个字符，因此从理论上来说可以覆盖U+0000到U+FFFF的字符。
 * 但是从 JDK 的Character API来看，支持从U+0000到U+10FFFF的字符
 * 详细参见 JDK Character class 文档
 * 2) 字符编码:
 * 字符编码是采用一种编码规则对某个字符集的某个字符的编号（code points）进行编码
 * 字符编码一定是关联着某个字符集的，脱离字符集来谈编码是没有意义的
 * 以Unicode字符集为例，有三种对应的字符编码，分别是UTF-8, UTF-16和UTF-32。
 * 平时我们接触的最多的是UTF-8编码，UTF-8的编码规则如下:
 * UTF-8是一种变长字节编码方式。对于某一个字符的UTF-8编码，如果只有一个字节则其最高二进制位为0；
 * 如果是多字节，其第一个字节从最高位开始，连续的二进制位值为1的个数决定了其编码的位数，其余各字节
 * 均以10开头。UTF-8最多可用到6个字节
 *
 * 下图展示了不同字节下的编码，x表示字符的内容，即所谓的payload
 * 1字节 0xxxxxxx
 * 2字节 110xxxxx 10xxxxxx
 * 3字节 1110xxxx 10xxxxxx 10xxxxxx
 * .....
 *
 */
public class CharEncoding {

    public static void main(String[] args) throws Exception {

        /** Java代码中可以使用 '\\u<十六进制>' 的形式引入一个Unicode的字符 **/
        char c1 = '\u56FD'; // 十进制为 22269
        char c2 = '国';
        // 上面这两种表示形式是一致的。另外可以使用下面的代码来检验Java是如何来表示一个Unicode字符的
        log.info("Unicode code points of 国 is: {}", (int) c2); // 22269
        // 也就是说Java内部其实就是使用Unicode的编号（code points）来表示一个Unicode字符的

        /**
         * 字符编码的定义：字符编码是采用一种编码规则对某个字符集的某个字符的编号（code points）进行编码。
         * 即用编码规则将字符的编号作为payload进行填充
         * 还是以汉字“国”来举例：“国”在Unicode的字符编号的十进制数字是22269，转化为二进制是：1010110 11111101，
         * 总共有15比特
         * 因此在UTF-8编码中需要使用3个字节来表示（1字节只有7比特的payload，2字节有11比特的payload）
         * 我们将“国”按照3字节的编码要求进行编码(从低位到高位填充)就得到了“国”的UTF-8编码（不足为以0补齐即可）：
         * 11100101 10011011 10111101
         * 我们用下面的代码来验证编码的正确性：
         */
        byte[] bArr = "国".getBytes("utf-8");
        for(byte b : bArr){
            System.out.print(Integer.toBinaryString(b & 0xFF));
            System.out.print(" ");
        }
        // 输出结果和我们预期的一致，11100101 10011011 10111101
    }

}
