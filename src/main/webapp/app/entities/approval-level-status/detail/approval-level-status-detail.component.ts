import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovalLevelStatus } from '../approval-level-status.model';

@Component({
  selector: 'jhi-approval-level-status-detail',
  templateUrl: './approval-level-status-detail.component.html',
})
export class ApprovalLevelStatusDetailComponent implements OnInit {
  approvalLevelStatus: IApprovalLevelStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalLevelStatus }) => {
      this.approvalLevelStatus = approvalLevelStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
