package cn.wdy07.model;

/**
 * @author wdy
 * @create 2020-06-20 23:48
 */
public enum MsgType {

    SERVICE_REQ((byte) 0),/*业务请求消息*/
    SERVICE_RESP((byte) 1), /*业务应答消息*/
    ONE_WAY((byte) 2), /*无需应答的消息*/
    LOGIN_REQ((byte) 3), /*登录请求消息*/
    LOGIN_RESP((byte) 4), /*登录响应消息*/
    HEARTBEAT_REQ((byte) 5), /*心跳请求消息*/
    HEARTBEAT_RESP((byte) 6);/*心跳应答消息*/

    private byte value;

    MsgType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
