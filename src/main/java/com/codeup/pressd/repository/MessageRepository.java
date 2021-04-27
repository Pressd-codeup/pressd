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
		mergeSort(result, 0, result.size() - 1);
		return result;
	}

	List<Message> findAllBySentTo(User user);

	/*
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

	 */


	static void merge(List<Message> messages, int a, int b, int c) {

		int n1 = b - a + 1;
		int n2 = c - b;


		Message[] left = new Message[n1];
		Message[] right = new Message[n2];


		for (int i = 0; i < n1; ++i) {
			left[i] = messages.get(a + i);
		}

		for (int j = 0; j < n2; ++j) {
			right[j] = messages.get(b + 1 + j);
		}


		int i = 0;
		int j = 0;
		int k = a;


		while (i < n1 && j < n2) {
			if (left[i].getDatePosted().isBefore(right[j].getDatePosted()) || left[i].getDatePosted().isEqual(right[j].getDatePosted())) {
				messages.set(k, left[i]);
				i++;
			} else {
				messages.set(k, right[j]);
				j++;
			}
			k++;
		}

		while (i < n1) {
			messages.set(k, left[i]);
			i++;
			k++;
		}

		while (j < n2) {
			messages.set(k, right[j]);
			j++;
			k++;
		}
	}

	static void mergeSort(List<Message> messages, int a, int b) {

		if (a < b) {

			int m = a + (b - a) / 2;

			mergeSort(messages, a, m);
			mergeSort(messages, m + 1, b);
			merge(messages, a, m, b);
		}
	}

}
