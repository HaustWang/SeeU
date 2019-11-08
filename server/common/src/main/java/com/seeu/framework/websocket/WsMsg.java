// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: wsMsg.proto

package com.seeu.framework.websocket;

public final class WsMsg {
  private WsMsg() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface wsMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.seeu.framework.websocket.wsMsg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *客户端请求或服务端消息唯一标识，请求和响应返回的id一致。
     * </pre>
     *
     * <code>int32 msgSeq = 1;</code>
     * @return The msgSeq.
     */
    int getMsgSeq();

    /**
     * <pre>
     *具体消息命令字定义，比如：user.login
     * </pre>
     *
     * <code>string msgId = 2;</code>
     * @return The msgId.
     */
    java.lang.String getMsgId();
    /**
     * <pre>
     *具体消息命令字定义，比如：user.login
     * </pre>
     *
     * <code>string msgId = 2;</code>
     * @return The bytes for msgId.
     */
    com.google.protobuf.ByteString
        getMsgIdBytes();

    /**
     * <pre>
     *消息内容。
     * </pre>
     *
     * <code>bytes content = 3;</code>
     * @return The content.
     */
    com.google.protobuf.ByteString getContent();
  }
  /**
   * Protobuf type {@code com.seeu.framework.websocket.wsMsg}
   */
  public  static final class wsMsg extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.seeu.framework.websocket.wsMsg)
      wsMsgOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use wsMsg.newBuilder() to construct.
    private wsMsg(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private wsMsg() {
      msgId_ = "";
      content_ = com.google.protobuf.ByteString.EMPTY;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new wsMsg();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private wsMsg(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              msgSeq_ = input.readInt32();
              break;
            }
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();

              msgId_ = s;
              break;
            }
            case 26: {

              content_ = input.readBytes();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.seeu.framework.websocket.WsMsg.internal_static_com_seeu_framework_websocket_wsMsg_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.seeu.framework.websocket.WsMsg.internal_static_com_seeu_framework_websocket_wsMsg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.seeu.framework.websocket.WsMsg.wsMsg.class, com.seeu.framework.websocket.WsMsg.wsMsg.Builder.class);
    }

    public static final int MSGSEQ_FIELD_NUMBER = 1;
    private int msgSeq_;
    /**
     * <pre>
     *客户端请求或服务端消息唯一标识，请求和响应返回的id一致。
     * </pre>
     *
     * <code>int32 msgSeq = 1;</code>
     * @return The msgSeq.
     */
    public int getMsgSeq() {
      return msgSeq_;
    }

    public static final int MSGID_FIELD_NUMBER = 2;
    private volatile java.lang.Object msgId_;
    /**
     * <pre>
     *具体消息命令字定义，比如：user.login
     * </pre>
     *
     * <code>string msgId = 2;</code>
     * @return The msgId.
     */
    public java.lang.String getMsgId() {
      java.lang.Object ref = msgId_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        msgId_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *具体消息命令字定义，比如：user.login
     * </pre>
     *
     * <code>string msgId = 2;</code>
     * @return The bytes for msgId.
     */
    public com.google.protobuf.ByteString
        getMsgIdBytes() {
      java.lang.Object ref = msgId_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        msgId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int CONTENT_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString content_;
    /**
     * <pre>
     *消息内容。
     * </pre>
     *
     * <code>bytes content = 3;</code>
     * @return The content.
     */
    public com.google.protobuf.ByteString getContent() {
      return content_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (msgSeq_ != 0) {
        output.writeInt32(1, msgSeq_);
      }
      if (!getMsgIdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, msgId_);
      }
      if (!content_.isEmpty()) {
        output.writeBytes(3, content_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (msgSeq_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, msgSeq_);
      }
      if (!getMsgIdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, msgId_);
      }
      if (!content_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, content_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.seeu.framework.websocket.WsMsg.wsMsg)) {
        return super.equals(obj);
      }
      com.seeu.framework.websocket.WsMsg.wsMsg other = (com.seeu.framework.websocket.WsMsg.wsMsg) obj;

      if (getMsgSeq()
          != other.getMsgSeq()) return false;
      if (!getMsgId()
          .equals(other.getMsgId())) return false;
      if (!getContent()
          .equals(other.getContent())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + MSGSEQ_FIELD_NUMBER;
      hash = (53 * hash) + getMsgSeq();
      hash = (37 * hash) + MSGID_FIELD_NUMBER;
      hash = (53 * hash) + getMsgId().hashCode();
      hash = (37 * hash) + CONTENT_FIELD_NUMBER;
      hash = (53 * hash) + getContent().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.seeu.framework.websocket.WsMsg.wsMsg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.seeu.framework.websocket.WsMsg.wsMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.seeu.framework.websocket.wsMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.seeu.framework.websocket.wsMsg)
        com.seeu.framework.websocket.WsMsg.wsMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.seeu.framework.websocket.WsMsg.internal_static_com_seeu_framework_websocket_wsMsg_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.seeu.framework.websocket.WsMsg.internal_static_com_seeu_framework_websocket_wsMsg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.seeu.framework.websocket.WsMsg.wsMsg.class, com.seeu.framework.websocket.WsMsg.wsMsg.Builder.class);
      }

      // Construct using com.seeu.framework.websocket.WsMsg.wsMsg.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        msgSeq_ = 0;

        msgId_ = "";

        content_ = com.google.protobuf.ByteString.EMPTY;

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.seeu.framework.websocket.WsMsg.internal_static_com_seeu_framework_websocket_wsMsg_descriptor;
      }

      @java.lang.Override
      public com.seeu.framework.websocket.WsMsg.wsMsg getDefaultInstanceForType() {
        return com.seeu.framework.websocket.WsMsg.wsMsg.getDefaultInstance();
      }

      @java.lang.Override
      public com.seeu.framework.websocket.WsMsg.wsMsg build() {
        com.seeu.framework.websocket.WsMsg.wsMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.seeu.framework.websocket.WsMsg.wsMsg buildPartial() {
        com.seeu.framework.websocket.WsMsg.wsMsg result = new com.seeu.framework.websocket.WsMsg.wsMsg(this);
        result.msgSeq_ = msgSeq_;
        result.msgId_ = msgId_;
        result.content_ = content_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.seeu.framework.websocket.WsMsg.wsMsg) {
          return mergeFrom((com.seeu.framework.websocket.WsMsg.wsMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.seeu.framework.websocket.WsMsg.wsMsg other) {
        if (other == com.seeu.framework.websocket.WsMsg.wsMsg.getDefaultInstance()) return this;
        if (other.getMsgSeq() != 0) {
          setMsgSeq(other.getMsgSeq());
        }
        if (!other.getMsgId().isEmpty()) {
          msgId_ = other.msgId_;
          onChanged();
        }
        if (other.getContent() != com.google.protobuf.ByteString.EMPTY) {
          setContent(other.getContent());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.seeu.framework.websocket.WsMsg.wsMsg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.seeu.framework.websocket.WsMsg.wsMsg) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int msgSeq_ ;
      /**
       * <pre>
       *客户端请求或服务端消息唯一标识，请求和响应返回的id一致。
       * </pre>
       *
       * <code>int32 msgSeq = 1;</code>
       * @return The msgSeq.
       */
      public int getMsgSeq() {
        return msgSeq_;
      }
      /**
       * <pre>
       *客户端请求或服务端消息唯一标识，请求和响应返回的id一致。
       * </pre>
       *
       * <code>int32 msgSeq = 1;</code>
       * @param value The msgSeq to set.
       * @return This builder for chaining.
       */
      public Builder setMsgSeq(int value) {
        
        msgSeq_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *客户端请求或服务端消息唯一标识，请求和响应返回的id一致。
       * </pre>
       *
       * <code>int32 msgSeq = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearMsgSeq() {
        
        msgSeq_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object msgId_ = "";
      /**
       * <pre>
       *具体消息命令字定义，比如：user.login
       * </pre>
       *
       * <code>string msgId = 2;</code>
       * @return The msgId.
       */
      public java.lang.String getMsgId() {
        java.lang.Object ref = msgId_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          msgId_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       *具体消息命令字定义，比如：user.login
       * </pre>
       *
       * <code>string msgId = 2;</code>
       * @return The bytes for msgId.
       */
      public com.google.protobuf.ByteString
          getMsgIdBytes() {
        java.lang.Object ref = msgId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          msgId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *具体消息命令字定义，比如：user.login
       * </pre>
       *
       * <code>string msgId = 2;</code>
       * @param value The msgId to set.
       * @return This builder for chaining.
       */
      public Builder setMsgId(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        msgId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *具体消息命令字定义，比如：user.login
       * </pre>
       *
       * <code>string msgId = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearMsgId() {
        
        msgId_ = getDefaultInstance().getMsgId();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *具体消息命令字定义，比如：user.login
       * </pre>
       *
       * <code>string msgId = 2;</code>
       * @param value The bytes for msgId to set.
       * @return This builder for chaining.
       */
      public Builder setMsgIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        msgId_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString content_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <pre>
       *消息内容。
       * </pre>
       *
       * <code>bytes content = 3;</code>
       * @return The content.
       */
      public com.google.protobuf.ByteString getContent() {
        return content_;
      }
      /**
       * <pre>
       *消息内容。
       * </pre>
       *
       * <code>bytes content = 3;</code>
       * @param value The content to set.
       * @return This builder for chaining.
       */
      public Builder setContent(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        content_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息内容。
       * </pre>
       *
       * <code>bytes content = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearContent() {
        
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.seeu.framework.websocket.wsMsg)
    }

    // @@protoc_insertion_point(class_scope:com.seeu.framework.websocket.wsMsg)
    private static final com.seeu.framework.websocket.WsMsg.wsMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.seeu.framework.websocket.WsMsg.wsMsg();
    }

    public static com.seeu.framework.websocket.WsMsg.wsMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<wsMsg>
        PARSER = new com.google.protobuf.AbstractParser<wsMsg>() {
      @java.lang.Override
      public wsMsg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new wsMsg(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<wsMsg> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<wsMsg> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.seeu.framework.websocket.WsMsg.wsMsg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_seeu_framework_websocket_wsMsg_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_seeu_framework_websocket_wsMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\013wsMsg.proto\022\034com.seeu.framework.websoc" +
      "ket\"7\n\005wsMsg\022\016\n\006msgSeq\030\001 \001(\005\022\r\n\005msgId\030\002 " +
      "\001(\t\022\017\n\007content\030\003 \001(\014B%\n\034com.seeu.framewo" +
      "rk.websocketB\005WsMsgb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_seeu_framework_websocket_wsMsg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_seeu_framework_websocket_wsMsg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_seeu_framework_websocket_wsMsg_descriptor,
        new java.lang.String[] { "MsgSeq", "MsgId", "Content", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}