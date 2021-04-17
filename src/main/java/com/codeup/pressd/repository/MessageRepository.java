package com.codeup.pressd.repository;

import com.codeup.pressd.models.Message;
import com.codeup.pressd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


	List<Message> findAllBySentFromOrSentTo(User sentFrom, User sentTo);

	List<Message> findAllBySentFromIsAndSentToIs(User sentFrom, User sentTo);

	default List<Message> makeThread(List<Message> list1, List<Message> list2) {
		List<Message> result = new ArrayList<>(list1);
		result.addAll(list2);
		quickSort(result, 0, result.size() - 1);
		return result;
	}

	static void quickSort(List<Message> messages, int low, int high) {
		if (low < high) {
			int pi = partition(messages, low, high);
			quickSort(messages, low, pi - 1);
			quickSort(messages, pi + 1, high);
		}
	}

	static int partition(List<Message> messages, int low, int high) {
		LocalDateTime pivot = messages.get(high).getDatePosted();
		int i = low - 1;

		for (int j = low; j <= high - 1; j++) {
			if (messages.get(j).getDatePosted().isBefore(pivot)) {
				i++;
				swap(messages, i, j);
			}
		}
		swap(messages, i + 1, high);
		return (i + 1);
	}

	static void swap(List<Message> messages, int i, int j) {
		Message temp = messages.get(i);
		messages.set(i, messages.get(j));
		messages.set(j, temp);
	}

}
