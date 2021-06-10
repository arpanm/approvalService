import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovalRule } from '../approval-rule.model';

@Component({
  selector: 'jhi-approval-rule-detail',
  templateUrl: './approval-rule-detail.component.html',
})
export class ApprovalRuleDetailComponent implements OnInit {
  approvalRule: IApprovalRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalRule }) => {
      this.approvalRule = approvalRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
