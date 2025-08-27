import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Feedback } from '../models/feedback.model';
import { Observable } from 'rxjs';
import { API_URL } from '../app.constant';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor(private client: HttpClient) { }

  sendFeedback(feedback: Feedback): Observable<Feedback> {
    return this.client.post(`${API_URL}/feedback`, feedback);
  }

  getAllFeedbacksByUserId(userId: number): Observable<Feedback[]> {
    return this.client.get<Feedback[]>(`${API_URL}/feedback/user/${userId}`);
  }

  deleteFeedback(feedbackId: number): Observable<void> {
    return this.client.delete<void>(`${API_URL}/feedback/${feedbackId}`);
  }

  getFeedbacks(): Observable<Feedback[]> {
    return this.client.get<Feedback[]>(`${API_URL}/feedback`);
  }

}
