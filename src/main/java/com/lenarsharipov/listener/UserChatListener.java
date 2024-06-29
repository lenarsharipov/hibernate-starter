package com.lenarsharipov.listener;

import com.lenarsharipov.entity.Chat;
import com.lenarsharipov.entity.UserChat;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;

public class UserChatListener {

    @PostPersist
    public void postPersist(UserChat userChat) {
        Chat chat = userChat.getChat();
        chat.setCount(chat.getCount() + 1);
    }

    @PostRemove
    public void postRemove(UserChat userChat) {
        Chat chat = userChat.getChat();
        chat.setCount(chat.getCount() - 1);
    }
}
