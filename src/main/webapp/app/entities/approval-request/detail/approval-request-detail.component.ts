import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovalRequest } from '../approval-request.model';

@Component({
  selector: 'jhi-approval-request-detail',
  templateUrl: './approval-request-detail.component.html',
})
export class ApprovalRequestDetailComponent implements OnInit {
  approvalRequest: IApprovalRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalRequest }) => {
      this.approvalRequest = approvalRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
