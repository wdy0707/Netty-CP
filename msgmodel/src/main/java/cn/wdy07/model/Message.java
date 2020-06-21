package cn.wdy07.model;

/**
 * @author wdy
 * @create 2020-06-20 23:54
 */
public final class Message {

    private MsgHeader header;

    private Object body;

    public Message(MsgHeader header, Object body) {
        this.header = header;
        this.body = body;
    }

    public Message() {
    }

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
