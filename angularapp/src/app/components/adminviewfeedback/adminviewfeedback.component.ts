import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Feedback } from 'src/app/models/feedback.model';
import { FeedbackService } from 'src/app/services/feedback.service';

@Component({
  selector: 'app-adminviewfeedback',
  templateUrl: './adminviewfeedback.component.html',
  styleUrls: ['./adminviewfeedback.component.css']
})
export class AdminviewfeedbackComponent implements OnInit, OnDestroy {

  feedbackSubscription:Subscription;

  feedbacks:Feedback[]=[];
  isLoading:boolean;
  
  getStars(rating: number): { filled: boolean }[] {
    return Array.from({ length: 5 }, (_, i) => ({ filled: i < rating }));
  }
  constructor(private service:FeedbackService) { }

  getFeedbacks(){
    this.feedbackSubscription = this.service.getFeedbacks().subscribe((result)=>{
      this.isLoading=true;
      this.feedbacks=result;
      console.log(this.feedbacks);
    },
    (error)=>{
      this.isLoading=true;
    })
    this.isLoading=false;
  }

  ngOnInit(): void {
    this.getFeedbacks();
  }

  ngOnDestroy():void{
    if(this.feedbackSubscription){
      this.feedbackSubscription.unsubscribe();
    }
  }

}
