import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Orders } from 'src/app/models/orders.model';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-adminorderschart',
  templateUrl: './adminorderschart.component.html',
  styleUrls: ['./adminorderschart.component.css']
})
export class AdminorderschartComponent implements OnInit, OnDestroy {

  getOrderSubscription:Subscription;
  orders: Orders[] = [];
  foodOrders: { name: string, count: number }[] = [];
  statusSummary: { label: string, count: number, color: string, icon: string }[] = [];

  selectedFood: string = '';
  selectedOrders: Orders[] = [];
  showModal: boolean = false;
  isDarkMode = false;
  

  // Bar Chart Config
  barChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      yAxes: [{ ticks: { beginAtZero: true, stepSize: 1 } }]
    }
  };
  barChartLabels: string[] = ['Delivered', 'Cancelled', 'Pending'];
  barChartData = [{ data: [], label: 'Orders by Status' }];
  barChartType = 'bar';

  // Line Chart Config
  lineChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      yAxes: [{ ticks: { beginAtZero: true } }]
    }
  };
  lineChartLabels: string[] = [];
  lineChartData = [{ data: [], label: 'Revenue Per Day' }];
  lineChartType = 'line';

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.getOrderSubscription = this.orderService.getAllOrders().subscribe((orders) => {
      this.orders = orders;
      this.generateOrderData();
      this.generateStatusSummary();
      this.calculateOrderStatusCounts();
      this.calculateRevenuePerDate();
    });
  }

  generateOrderData() {
    const map = new Map<string, number>();
    this.orders.forEach(order => {
      const name = order.food?.foodName || 'Unknown';
      if (order.orderStatus === 'Delivered') {
        map.set(name, (map.get(name) || 0) + 1);
      }
    });

    this.foodOrders = Array.from(map.entries()).map(([name, count]) => ({ name, count }));
  }

  generateStatusSummary() {
    const statusMap = new Map<string, number>();
    this.orders.forEach(order => {
      const status = order.orderStatus || 'Unknown';
      statusMap.set(status, (statusMap.get(status) || 0) + 1);
    });

    this.statusSummary = [
      { label: 'Pending', count: statusMap.get('Pending') || 0, color: 'text-warning', icon: 'bi-hourglass-split' },
      { label: 'Delivered', count: statusMap.get('Delivered') || 0, color: 'text-success', icon: 'bi-check-circle' },
      { label: 'Cancelled', count: statusMap.get('Cancelled') || 0, color: 'text-danger', icon: 'bi-x-circle' },
      { label: 'Making Food', count: statusMap.get('Making Food') || 0, color: 'text-primary', icon: 'bi-gear' },
      { label: 'On the Way', count: statusMap.get('On the way') || 0, color: 'text-info', icon: 'bi-truck' }
    ];
  }

  calculateOrderStatusCounts() {
    const counts = { 'Delivered': 0, 'Cancelled': 0, 'Pending': 0 };
    this.orders.forEach(order => {
      const status = order.orderStatus?.trim();
      if (status && counts.hasOwnProperty(status)) {
        counts[status]++;
      }
    });
    this.barChartData[0].data = [
      counts['Delivered'],
      counts['Cancelled'],
      counts['Pending']
    ];
  }

  calculateRevenuePerDate() {
    const revenueMap: { [key: string]: number } = {};
    this.orders.forEach(order => {
      const date = order.orderDate?.split('T')[0];
      const amount = order.totalAmount || 0;
      if (date) {
        revenueMap[date] = (revenueMap[date] || 0) + amount;
      }
    });
    this.lineChartLabels = Object.keys(revenueMap).sort();
    this.lineChartData[0].data = this.lineChartLabels.map(date => revenueMap[date]);
  }

  openFoodOrders(foodName: string) {
    console.log('Clicked on: ', foodName);
    this.selectedFood = foodName;
    this.selectedOrders = this.orders.filter(order =>
      order.food?.foodName === foodName && order.orderStatus === 'Delivered'
    );
    console.log('Filtered Orders:', this.selectedOrders);
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  ngOnDestroy():void{
    if(this.getOrderSubscription){
      this.getOrderSubscription.unsubscribe();
    }
  }

}