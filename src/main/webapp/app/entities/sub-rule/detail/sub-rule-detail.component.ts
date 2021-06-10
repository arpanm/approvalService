import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubRule } from '../sub-rule.model';

@Component({
  selector: 'jhi-sub-rule-detail',
  templateUrl: './sub-rule-detail.component.html',
})
export class SubRuleDetailComponent implements OnInit {
  subRule: ISubRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subRule }) => {
      this.subRule = subRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
