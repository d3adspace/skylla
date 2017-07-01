/*
 * Copyright (c) 2017 D3adspace
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.d3adspace.skylla.commons.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

/**
 * Wrapper for the netty bytebuf.
 *
 * @author Nathalie0hneHerz
 */
public class SkyllaBuffer extends ByteBuf {
	
	/**
	 * THe underlying byteBuf - Credits to the lovely maven crew for this epic buffer handling.
	 */
	private final ByteBuf handle;
	
	public SkyllaBuffer(ByteBuf handle) {
		this.handle = handle;
	}
	
	@Override
	public int capacity() {
		return handle.capacity();
	}
	
	@Override
	public ByteBuf capacity(int i) {
		return handle.capacity(i);
	}
	
	@Override
	public int maxCapacity() {
		return handle.maxCapacity();
	}
	
	@Override
	public ByteBufAllocator alloc() {
		return handle.alloc();
	}
	
	@Override
	@Deprecated
	public ByteOrder order() {
		return handle.order();
	}
	
	@Override
	@Deprecated
	public ByteBuf order(ByteOrder byteOrder) {
		return handle.order(byteOrder);
	}
	
	@Override
	public ByteBuf unwrap() {
		return handle.unwrap();
	}
	
	@Override
	public boolean isDirect() {
		return handle.isDirect();
	}
	
	@Override
	public boolean isReadOnly() {
		return handle.isReadOnly();
	}
	
	@Override
	public ByteBuf asReadOnly() {
		return handle.asReadOnly();
	}
	
	@Override
	public int readerIndex() {
		return handle.readerIndex();
	}
	
	@Override
	public ByteBuf readerIndex(int i) {
		return handle.readerIndex(i);
	}
	
	@Override
	public int writerIndex() {
		return handle.writerIndex();
	}
	
	@Override
	public ByteBuf writerIndex(int i) {
		return handle.writerIndex(i);
	}
	
	@Override
	public ByteBuf setIndex(int i, int i1) {
		return handle.setIndex(i, i1);
	}
	
	@Override
	public int readableBytes() {
		return handle.readableBytes();
	}
	
	@Override
	public int writableBytes() {
		return handle.writableBytes();
	}
	
	@Override
	public int maxWritableBytes() {
		return handle.maxWritableBytes();
	}
	
	@Override
	public boolean isReadable() {
		return handle.isReadable();
	}
	
	@Override
	public boolean isReadable(int i) {
		return handle.isReadable(i);
	}
	
	@Override
	public boolean isWritable() {
		return handle.isWritable();
	}
	
	@Override
	public boolean isWritable(int i) {
		return handle.isWritable(i);
	}
	
	@Override
	public ByteBuf clear() {
		return handle.clear();
	}
	
	@Override
	public ByteBuf markReaderIndex() {
		return handle.markReaderIndex();
	}
	
	@Override
	public ByteBuf resetReaderIndex() {
		return handle.resetReaderIndex();
	}
	
	@Override
	public ByteBuf markWriterIndex() {
		return handle.markWriterIndex();
	}
	
	@Override
	public ByteBuf resetWriterIndex() {
		return handle.resetWriterIndex();
	}
	
	@Override
	public ByteBuf discardReadBytes() {
		return handle.discardReadBytes();
	}
	
	@Override
	public ByteBuf discardSomeReadBytes() {
		return handle.discardSomeReadBytes();
	}
	
	@Override
	public ByteBuf ensureWritable(int i) {
		return handle.ensureWritable(i);
	}
	
	@Override
	public int ensureWritable(int i, boolean b) {
		return handle.ensureWritable(i, b);
	}
	
	@Override
	public boolean getBoolean(int i) {
		return handle.getBoolean(i);
	}
	
	@Override
	public byte getByte(int i) {
		return handle.getByte(i);
	}
	
	@Override
	public short getUnsignedByte(int i) {
		return handle.getUnsignedByte(i);
	}
	
	@Override
	public short getShort(int i) {
		return handle.getShort(i);
	}
	
	@Override
	public short getShortLE(int i) {
		return handle.getShortLE(i);
	}
	
	@Override
	public int getUnsignedShort(int i) {
		return handle.getUnsignedShort(i);
	}
	
	@Override
	public int getUnsignedShortLE(int i) {
		return handle.getUnsignedShortLE(i);
	}
	
	@Override
	public int getMedium(int i) {
		return handle.getMedium(i);
	}
	
	@Override
	public int getMediumLE(int i) {
		return handle.getMediumLE(i);
	}
	
	@Override
	public int getUnsignedMedium(int i) {
		return handle.getUnsignedMedium(i);
	}
	
	@Override
	public int getUnsignedMediumLE(int i) {
		return handle.getUnsignedMediumLE(i);
	}
	
	@Override
	public int getInt(int i) {
		return handle.getInt(i);
	}
	
	@Override
	public int getIntLE(int i) {
		return handle.getIntLE(i);
	}
	
	@Override
	public long getUnsignedInt(int i) {
		return handle.getUnsignedInt(i);
	}
	
	@Override
	public long getUnsignedIntLE(int i) {
		return handle.getUnsignedIntLE(i);
	}
	
	@Override
	public long getLong(int i) {
		return handle.getLong(i);
	}
	
	@Override
	public long getLongLE(int i) {
		return handle.getLongLE(i);
	}
	
	@Override
	public char getChar(int i) {
		return handle.getChar(i);
	}
	
	@Override
	public float getFloat(int i) {
		return handle.getFloat(i);
	}
	
	@Override
	public double getDouble(int i) {
		return handle.getDouble(i);
	}
	
	@Override
	public ByteBuf getBytes(int i, ByteBuf byteBuf) {
		return handle.getBytes(i, byteBuf);
	}
	
	@Override
	public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1) {
		return handle.getBytes(i, byteBuf, i1);
	}
	
	@Override
	public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1, int i2) {
		return handle.getBytes(i, byteBuf, i1, i2);
	}
	
	@Override
	public ByteBuf getBytes(int i, byte[] bytes) {
		return handle.getBytes(i, bytes);
	}
	
	@Override
	public ByteBuf getBytes(int i, byte[] bytes, int i1, int i2) {
		return handle.getBytes(i, bytes, i1, i2);
	}
	
	@Override
	public ByteBuf getBytes(int i, ByteBuffer byteBuffer) {
		return handle.getBytes(i, byteBuffer);
	}
	
	@Override
	public ByteBuf getBytes(int i, OutputStream outputStream, int i1) throws IOException {
		return handle.getBytes(i, outputStream, i1);
	}
	
	@Override
	public int getBytes(int i, GatheringByteChannel gatheringByteChannel, int i1)
		throws IOException {
		return handle.getBytes(i, gatheringByteChannel, i1);
	}
	
	@Override
	public int getBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
		return handle.getBytes(i, fileChannel, l, i1);
	}
	
	@Override
	public CharSequence getCharSequence(int i, int i1, Charset charset) {
		return handle.getCharSequence(i, i1, charset);
	}
	
	@Override
	public ByteBuf setBoolean(int i, boolean b) {
		return handle.setBoolean(i, b);
	}
	
	@Override
	public ByteBuf setByte(int i, int i1) {
		return handle.setByte(i, i1);
	}
	
	@Override
	public ByteBuf setShort(int i, int i1) {
		return handle.setShort(i, i1);
	}
	
	@Override
	public ByteBuf setShortLE(int i, int i1) {
		return handle.setShortLE(i, i1);
	}
	
	@Override
	public ByteBuf setMedium(int i, int i1) {
		return handle.setMedium(i, i1);
	}
	
	@Override
	public ByteBuf setMediumLE(int i, int i1) {
		return handle.setMediumLE(i, i1);
	}
	
	@Override
	public ByteBuf setInt(int i, int i1) {
		return handle.setInt(i, i1);
	}
	
	@Override
	public ByteBuf setIntLE(int i, int i1) {
		return handle.setIntLE(i, i1);
	}
	
	@Override
	public ByteBuf setLong(int i, long l) {
		return handle.setLong(i, l);
	}
	
	@Override
	public ByteBuf setLongLE(int i, long l) {
		return handle.setLongLE(i, l);
	}
	
	@Override
	public ByteBuf setChar(int i, int i1) {
		return handle.setChar(i, i1);
	}
	
	@Override
	public ByteBuf setFloat(int i, float v) {
		return handle.setFloat(i, v);
	}
	
	@Override
	public ByteBuf setDouble(int i, double v) {
		return handle.setDouble(i, v);
	}
	
	@Override
	public ByteBuf setBytes(int i, ByteBuf byteBuf) {
		return handle.setBytes(i, byteBuf);
	}
	
	@Override
	public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1) {
		return handle.setBytes(i, byteBuf, i1);
	}
	
	@Override
	public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1, int i2) {
		return handle.setBytes(i, byteBuf, i1, i2);
	}
	
	@Override
	public ByteBuf setBytes(int i, byte[] bytes) {
		return handle.setBytes(i, bytes);
	}
	
	@Override
	public ByteBuf setBytes(int i, byte[] bytes, int i1, int i2) {
		return handle.setBytes(i, bytes, i1, i2);
	}
	
	@Override
	public ByteBuf setBytes(int i, ByteBuffer byteBuffer) {
		return handle.setBytes(i, byteBuffer);
	}
	
	@Override
	public int setBytes(int i, InputStream inputStream, int i1) throws IOException {
		return handle.setBytes(i, inputStream, i1);
	}
	
	@Override
	public int setBytes(int i, ScatteringByteChannel scatteringByteChannel, int i1)
		throws IOException {
		return handle.setBytes(i, scatteringByteChannel, i1);
	}
	
	@Override
	public int setBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
		return handle.setBytes(i, fileChannel, l, i1);
	}
	
	@Override
	public ByteBuf setZero(int i, int i1) {
		return handle.setZero(i, i1);
	}
	
	@Override
	public int setCharSequence(int i, CharSequence charSequence, Charset charset) {
		return handle.setCharSequence(i, charSequence, charset);
	}
	
	@Override
	public boolean readBoolean() {
		return handle.readBoolean();
	}
	
	@Override
	public byte readByte() {
		return handle.readByte();
	}
	
	@Override
	public short readUnsignedByte() {
		return handle.readUnsignedByte();
	}
	
	@Override
	public short readShort() {
		return handle.readShort();
	}
	
	@Override
	public short readShortLE() {
		return handle.readShortLE();
	}
	
	@Override
	public int readUnsignedShort() {
		return handle.readUnsignedShort();
	}
	
	@Override
	public int readUnsignedShortLE() {
		return handle.readUnsignedShortLE();
	}
	
	@Override
	public int readMedium() {
		return handle.readMedium();
	}
	
	@Override
	public int readMediumLE() {
		return handle.readMediumLE();
	}
	
	@Override
	public int readUnsignedMedium() {
		return handle.readUnsignedMedium();
	}
	
	@Override
	public int readUnsignedMediumLE() {
		return handle.readUnsignedMediumLE();
	}
	
	@Override
	public int readInt() {
		return handle.readInt();
	}
	
	@Override
	public int readIntLE() {
		return handle.readIntLE();
	}
	
	@Override
	public long readUnsignedInt() {
		return handle.readUnsignedInt();
	}
	
	@Override
	public long readUnsignedIntLE() {
		return handle.readUnsignedIntLE();
	}
	
	@Override
	public long readLong() {
		return handle.readLong();
	}
	
	@Override
	public long readLongLE() {
		return handle.readLongLE();
	}
	
	@Override
	public char readChar() {
		return handle.readChar();
	}
	
	@Override
	public float readFloat() {
		return handle.readFloat();
	}
	
	@Override
	public double readDouble() {
		return handle.readDouble();
	}
	
	@Override
	public ByteBuf readBytes(int i) {
		return handle.readBytes(i);
	}
	
	@Override
	public ByteBuf readSlice(int i) {
		return handle.readSlice(i);
	}
	
	@Override
	public ByteBuf readRetainedSlice(int i) {
		return handle.readRetainedSlice(i);
	}
	
	@Override
	public ByteBuf readBytes(ByteBuf byteBuf) {
		return handle.readBytes(byteBuf);
	}
	
	@Override
	public ByteBuf readBytes(ByteBuf byteBuf, int i) {
		return handle.readBytes(byteBuf, i);
	}
	
	@Override
	public ByteBuf readBytes(ByteBuf byteBuf, int i, int i1) {
		return handle.readBytes(byteBuf, i, i1);
	}
	
	@Override
	public ByteBuf readBytes(byte[] bytes) {
		return handle.readBytes(bytes);
	}
	
	@Override
	public ByteBuf readBytes(byte[] bytes, int i, int i1) {
		return handle.readBytes(bytes, i, i1);
	}
	
	@Override
	public ByteBuf readBytes(ByteBuffer byteBuffer) {
		return handle.readBytes(byteBuffer);
	}
	
	@Override
	public ByteBuf readBytes(OutputStream outputStream, int i) throws IOException {
		return handle.readBytes(outputStream, i);
	}
	
	@Override
	public int readBytes(GatheringByteChannel gatheringByteChannel, int i) throws IOException {
		return handle.readBytes(gatheringByteChannel, i);
	}
	
	@Override
	public CharSequence readCharSequence(int i, Charset charset) {
		return handle.readCharSequence(i, charset);
	}
	
	@Override
	public int readBytes(FileChannel fileChannel, long l, int i) throws IOException {
		return handle.readBytes(fileChannel, l, i);
	}
	
	@Override
	public ByteBuf skipBytes(int i) {
		return handle.skipBytes(i);
	}
	
	@Override
	public ByteBuf writeBoolean(boolean b) {
		return handle.writeBoolean(b);
	}
	
	@Override
	public ByteBuf writeByte(int i) {
		return handle.writeByte(i);
	}
	
	@Override
	public ByteBuf writeShort(int i) {
		return handle.writeShort(i);
	}
	
	@Override
	public ByteBuf writeShortLE(int i) {
		return handle.writeShortLE(i);
	}
	
	@Override
	public ByteBuf writeMedium(int i) {
		return handle.writeMedium(i);
	}
	
	@Override
	public ByteBuf writeMediumLE(int i) {
		return handle.writeMediumLE(i);
	}
	
	@Override
	public ByteBuf writeInt(int i) {
		return handle.writeInt(i);
	}
	
	@Override
	public ByteBuf writeIntLE(int i) {
		return handle.writeIntLE(i);
	}
	
	@Override
	public ByteBuf writeLong(long l) {
		return handle.writeLong(l);
	}
	
	@Override
	public ByteBuf writeLongLE(long l) {
		return handle.writeLongLE(l);
	}
	
	@Override
	public ByteBuf writeChar(int i) {
		return handle.writeChar(i);
	}
	
	@Override
	public ByteBuf writeFloat(float v) {
		return handle.writeFloat(v);
	}
	
	@Override
	public ByteBuf writeDouble(double v) {
		return handle.writeDouble(v);
	}
	
	@Override
	public ByteBuf writeBytes(ByteBuf byteBuf) {
		return handle.writeBytes(byteBuf);
	}
	
	@Override
	public ByteBuf writeBytes(ByteBuf byteBuf, int i) {
		return handle.writeBytes(byteBuf, i);
	}
	
	@Override
	public ByteBuf writeBytes(ByteBuf byteBuf, int i, int i1) {
		return handle.writeBytes(byteBuf, i, i1);
	}
	
	@Override
	public ByteBuf writeBytes(byte[] bytes) {
		return handle.writeBytes(bytes);
	}
	
	@Override
	public ByteBuf writeBytes(byte[] bytes, int i, int i1) {
		return handle.writeBytes(bytes, i, i1);
	}
	
	@Override
	public ByteBuf writeBytes(ByteBuffer byteBuffer) {
		return handle.writeBytes(byteBuffer);
	}
	
	@Override
	public int writeBytes(InputStream inputStream, int i) throws IOException {
		return handle.writeBytes(inputStream, i);
	}
	
	@Override
	public int writeBytes(ScatteringByteChannel scatteringByteChannel, int i) throws IOException {
		return handle.writeBytes(scatteringByteChannel, i);
	}
	
	@Override
	public int writeBytes(FileChannel fileChannel, long l, int i) throws IOException {
		return handle.writeBytes(fileChannel, l, i);
	}
	
	@Override
	public ByteBuf writeZero(int i) {
		return handle.writeZero(i);
	}
	
	@Override
	public int writeCharSequence(CharSequence charSequence, Charset charset) {
		return handle.writeCharSequence(charSequence, charset);
	}
	
	@Override
	public int indexOf(int i, int i1, byte b) {
		return handle.indexOf(i, i1, b);
	}
	
	@Override
	public int bytesBefore(byte b) {
		return handle.bytesBefore(b);
	}
	
	@Override
	public int bytesBefore(int i, byte b) {
		return handle.bytesBefore(i, b);
	}
	
	@Override
	public int bytesBefore(int i, int i1, byte b) {
		return handle.bytesBefore(i, i1, b);
	}
	
	@Override
	public int forEachByte(ByteProcessor byteProcessor) {
		return handle.forEachByte(byteProcessor);
	}
	
	@Override
	public int forEachByte(int i, int i1, ByteProcessor byteProcessor) {
		return handle.forEachByte(i, i1, byteProcessor);
	}
	
	@Override
	public int forEachByteDesc(ByteProcessor byteProcessor) {
		return handle.forEachByteDesc(byteProcessor);
	}
	
	@Override
	public int forEachByteDesc(int i, int i1, ByteProcessor byteProcessor) {
		return handle.forEachByteDesc(i, i1, byteProcessor);
	}
	
	@Override
	public ByteBuf copy() {
		return handle.copy();
	}
	
	@Override
	public ByteBuf copy(int i, int i1) {
		return handle.copy(i, i1);
	}
	
	@Override
	public ByteBuf slice() {
		return handle.slice();
	}
	
	@Override
	public ByteBuf retainedSlice() {
		return handle.retainedSlice();
	}
	
	@Override
	public ByteBuf slice(int i, int i1) {
		return handle.slice(i, i1);
	}
	
	@Override
	public ByteBuf retainedSlice(int i, int i1) {
		return handle.retainedSlice(i, i1);
	}
	
	@Override
	public ByteBuf duplicate() {
		return handle.duplicate();
	}
	
	@Override
	public ByteBuf retainedDuplicate() {
		return handle.retainedDuplicate();
	}
	
	@Override
	public int nioBufferCount() {
		return handle.nioBufferCount();
	}
	
	@Override
	public ByteBuffer nioBuffer() {
		return handle.nioBuffer();
	}
	
	@Override
	public ByteBuffer nioBuffer(int i, int i1) {
		return handle.nioBuffer(i, i1);
	}
	
	@Override
	public ByteBuffer internalNioBuffer(int i, int i1) {
		return handle.internalNioBuffer(i, i1);
	}
	
	@Override
	public ByteBuffer[] nioBuffers() {
		return handle.nioBuffers();
	}
	
	@Override
	public ByteBuffer[] nioBuffers(int i, int i1) {
		return handle.nioBuffers(i, i1);
	}
	
	@Override
	public boolean hasArray() {
		return handle.hasArray();
	}
	
	@Override
	public byte[] array() {
		return handle.array();
	}
	
	@Override
	public int arrayOffset() {
		return handle.arrayOffset();
	}
	
	@Override
	public boolean hasMemoryAddress() {
		return handle.hasMemoryAddress();
	}
	
	@Override
	public long memoryAddress() {
		return handle.memoryAddress();
	}
	
	@Override
	public String toString(Charset charset) {
		return handle.toString(charset);
	}
	
	@Override
	public String toString(int i, int i1, Charset charset) {
		return handle.toString(i, i1, charset);
	}
	
	@Override
	public int hashCode() {
		return handle.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return handle.equals(o);
	}
	
	@Override
	public int compareTo(ByteBuf byteBuf) {
		return handle.compareTo(byteBuf);
	}
	
	@Override
	public String toString() {
		return handle.toString();
	}
	
	@Override
	public ByteBuf retain(int i) {
		return handle.retain(i);
	}
	
	@Override
	public ByteBuf retain() {
		return handle.retain();
	}
	
	@Override
	public ByteBuf touch() {
		return handle.touch();
	}
	
	@Override
	public ByteBuf touch(Object o) {
		return handle.touch(o);
	}
	
	@Override
	public int refCnt() {
		return handle.refCnt();
	}
	
	@Override
	public boolean release() {
		return handle.release();
	}
	
	@Override
	public boolean release(int i) {
		return handle.release(i);
	}
	
	public String readString() {
		int length = handle.readInt();
		byte[] bytes = new byte[length];
		
		handle.readBytes(bytes);
		
		return new String(bytes);
	}
	
	public void writeString(String message) {
		byte[] bytes = message.getBytes();
		
		handle.writeInt(bytes.length);
		handle.writeBytes(bytes);
	}
}
