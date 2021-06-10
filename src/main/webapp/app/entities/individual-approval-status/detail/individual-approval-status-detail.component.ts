import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIndividualApprovalStatus } from '../individual-approval-status.model';

@Component({
  selector: 'jhi-individual-approval-status-detail',
  templateUrl: './individual-approval-status-detail.component.html',
})
export class IndividualApprovalStatusDetailComponent implements OnInit {
  individualApprovalStatus: IIndividualApprovalStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ individualApprovalStatus }) => {
      this.individualApprovalStatus = individualApprovalStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
