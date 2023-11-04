package com.dompoo.onlineshopping.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConversation is a Querydsl query type for Conversation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConversation extends EntityPathBase<Conversation> {

    private static final long serialVersionUID = 1550656461L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConversation conversation = new QConversation("conversation");

    public final ListPath<Chat, QChat> chats = this.<Chat, QChat>createList("chats", Chat.class, QChat.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPost post;

    public QConversation(String variable) {
        this(Conversation.class, forVariable(variable), INITS);
    }

    public QConversation(Path<? extends Conversation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConversation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConversation(PathMetadata metadata, PathInits inits) {
        this(Conversation.class, metadata, inits);
    }

    public QConversation(Class<? extends Conversation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

