import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprover } from '../approver.model';

@Component({
  selector: 'jhi-approver-detail',
  templateUrl: './approver-detail.component.html',
})
export class ApproverDetailComponent implements OnInit {
  approver: IApprover | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approver }) => {
      this.approver = approver;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
