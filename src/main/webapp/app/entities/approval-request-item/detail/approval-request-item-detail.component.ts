import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovalRequestItem } from '../approval-request-item.model';

@Component({
  selector: 'jhi-approval-request-item-detail',
  templateUrl: './approval-request-item-detail.component.html',
})
export class ApprovalRequestItemDetailComponent implements OnInit {
  approvalRequestItem: IApprovalRequestItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalRequestItem }) => {
      this.approvalRequestItem = approvalRequestItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
