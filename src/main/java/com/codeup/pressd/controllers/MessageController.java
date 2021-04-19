package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Message;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.MessageRepository;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    private final MessageRepository messageDao;
    private final UserRepository userDao;

    MessageController(MessageRepository messageDao, UserRepository userDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
    }

    @GetMapping("/messages")
    public String viewMessageCenter(Model viewModel) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Message> messages = messageDao.findAllBySentFromOrSentTo(currentUser, currentUser);
        List<User> threads = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSentFrom().getUsername().equals(currentUser.getUsername())) {
                if (!threads.contains(message.getSentTo())) threads.add(message.getSentTo());
            } else {
                if (!threads.contains(message.getSentFrom())) threads.add(message.getSentFrom());
            }
        }
        viewModel.addAttribute("threads", threads);
        return "messages/index";
    }

    @GetMapping("/messages/{id}")
    public String showMessageThread(Model viewModel, @PathVariable long id) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User otherUser = userDao.getOne(id);

        List<Message> m1 = messageDao.findAllBySentFromIsAndSentToIs(currentUser, otherUser);
        List<Message> m2 = messageDao.findAllBySentFromIsAndSentToIs(otherUser, currentUser);
        List<Message> messages = messageDao.makeThread(m1, m2);
        DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        viewModel.addAttribute("messages", messages);
        viewModel.addAttribute("shortF", shortF);
        viewModel.addAttribute("currentUser", currentUser);
        return "messages/thread";
    }

    @GetMapping("/messages/send")
    public String sendNew(Model viewModel) {
        viewModel.addAttribute("message", new Message());
        return "messages/send";
    }

    @PostMapping("/messages/send")
    public String sendMsg(@ModelAttribute Message message, @RequestParam(name = "to_id") long to_id){
        User from = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User to = userDao.getOne(to_id);
      message.setSentFrom(from);
      message.setSentTo(to);
      message.setDatePosted(LocalDateTime.now());
      messageDao.save(message);

        return "redirect:/messages";
    }
}
