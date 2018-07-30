package com.github.ryan.component.netty.iterate_pattern;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: IteratorDemo
 * @date July 27,2018
 */
public class IteratorDemo {


    public static void main(String[] args) {

        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{1, 2, 3});
        ByteBuf body = Unpooled.wrappedBuffer(new byte[]{4, 5, 6});
        ByteBuf byteBuf = mergeByteBuf(header, body);

        // 先通过二分查找，找到index对应的封装了ByteBuf的Component，然后再在ByteBuf
        // 中找到对应的字节数据；使用 CompositeByteBuf避免了无用的内存拷贝(把ByteBuf通过内存
        // 拷贝得到容量更大的ByteBuf)
        byteBuf.forEachByte(value -> {
            System.out.println(value);
            return true;
        });

        System.out.println("--------------------------------------");

        // 迭代器迭代 CompositeByteBuf(Iterable)中的每一个ByteBuf
        for (ByteBuf buf : (CompositeByteBuf) byteBuf) {
            buf.forEachByte(value -> {
                System.out.println(value);
                return true;
            });
        }
    }

    public static ByteBuf mergeByteBuf(ByteBuf header, ByteBuf body) {
        // 存在大量拷贝
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
//        byteBuf.writeBytes(header);
//        byteBuf.writeBytes(body);
//        return byteBuf;

        // 使用CompositeByteBuf避免无用拷贝(CompositeByteBuf:逻辑上的ByteBuf)
        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer(2);
        compositeByteBuf.addComponent(false, header);
        compositeByteBuf.addComponent(true, body);
        return compositeByteBuf;
    }

}
