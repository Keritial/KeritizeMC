//package io.github.keritial.keritize.velocity
//
//import com.velocitypowered.api.event.EventHandler
//import com.velocitypowered.api.event.EventTask
//import com.velocitypowered.api.event.connection.PluginMessageEvent
//import com.velocitypowered.api.proxy.Player
//import com.velocitypowered.api.proxy.ServerConnection
//import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
//import io.netty.buffer.ByteBuf
//import io.netty.buffer.Unpooled
//import java.nio.charset.StandardCharsets
//
//class BrandListener : EventHandler<PluginMessageEvent> {
//    override fun execute(event: PluginMessageEvent) {
//            // 仅处理从服务器发送到客户端的品牌信息
//            if (event.source !is ServerConnection) return
//            if (event.target !is Player) return
//            if (event.identifier != BRAND_CHANNEL) return
//
//            // 修改品牌信息为 "vanilla"
//            val player = event.target as Player
//            val originalData: ByteBuf = Unpooled.wrappedBuffer(event.data)
//            val originalBrand = readBrandString(originalData)
//
//            // 创建修改后的数据
//            val modifiedData: ByteBuf = Unpooled.buffer()
//            writeBrandString(modifiedData, "vanilla")
//
//            // 替换数据包内容
//            event.result = PluginMessageEvent.ForwardResult.handled()
//            player.sendPluginMessage(BRAND_CHANNEL, modifiedData.array())
//        }
//
//    // 读取品牌字符串（处理不同版本的数据格式）
//    private fun readBrandString(buffer: ByteBuf): String {
//        if (buffer.readableBytes() <= 0) return ""
//        val length: Int = buffer.readByte() // 字符串长度（1字节）
//        val bytes = ByteArray(length)
//        buffer.readBytes(bytes)
//        return String(bytes, StandardCharsets.UTF_8)
//    }
//
//    // 写入品牌字符串（兼容 1.13+）
//    private fun writeBrandString(buffer: ByteBuf, brand: String) {
//        val bytes = brand.toByteArray(StandardCharsets.UTF_8)
//        buffer.writeByte(bytes.size) // 写入长度（1字节）
//        buffer.writeBytes(bytes)
//    }
//
//    companion object {
//        private val BRAND_CHANNEL: MinecraftChannelIdentifier = MinecraftChannelIdentifier.create("minecraft", "brand")
//    }
//}