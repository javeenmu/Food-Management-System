import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Feedback } from 'src/app/models/feedback.model';
import { FeedbackService } from 'src/app/services/feedback.service';

@Component({
  selector: 'app-userviewfeedback',
  templateUrl: './userviewfeedback.component.html',
  styleUrls: ['./userviewfeedback.component.css']
})
export class UserviewfeedbackComponent implements OnInit,OnDestroy {
  feedbackList = [];
  showPopup: boolean = false;
  selectedFeedbackId: number | null = null;
  isLoading:boolean=false;
  deleteFeedbackSubscription:Subscription;
  getFeedbackSubscription:Subscription;
  getStars(rating: number): { filled: boolean }[] {
    return Array.from({ length: 5 }, (_, i) => ({ filled: i < rating }));
  }

  openDeleteDialog(id: number) {
    this.selectedFeedbackId = id;
    this.showPopup = true;
  }

  confirmDelete() {
    if (this.selectedFeedbackId !== null) {
      this.deleteFeedbackSubscription=this.feedbackService.deleteFeedback(this.selectedFeedbackId).subscribe((result) => {
        console.log("feed back deleted succesfully");
        this.ngOnInit();
        this.showPopup = false;
      })

    }
  }

  cancelDelete() {
    this.selectedFeedbackId = null;
    this.showPopup = false;
    this.ngOnInit()
  }

  constructor(private feedbackService: FeedbackService) { }
  
  
  
  ngOnInit(): void {
    
    this.getFeedbackSubscription=this.feedbackService.getAllFeedbacksByUserId(+sessionStorage.getItem('userId')).subscribe((result) => {
      this.feedbackList = result;
      this.isLoading=true;
    },(error)=>{
      this.isLoading=false;
      console.log(error.error);
    })
    this.isLoading=false;
  }
  ngOnDestroy(): void {
      if(this.deleteFeedbackSubscription)
      this.deleteFeedbackSubscription.unsubscribe();
    if(this.getFeedbackSubscription)
    this.getFeedbackSubscription.unsubscribe();
  }
  
}
