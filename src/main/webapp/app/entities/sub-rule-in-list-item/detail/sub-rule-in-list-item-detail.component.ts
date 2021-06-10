import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubRuleInListItem } from '../sub-rule-in-list-item.model';

@Component({
  selector: 'jhi-sub-rule-in-list-item-detail',
  templateUrl: './sub-rule-in-list-item-detail.component.html',
})
export class SubRuleInListItemDetailComponent implements OnInit {
  subRuleInListItem: ISubRuleInListItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subRuleInListItem }) => {
      this.subRuleInListItem = subRuleInListItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
