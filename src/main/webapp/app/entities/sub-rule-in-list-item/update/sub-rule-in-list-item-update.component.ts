import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubRuleInListItem, SubRuleInListItem } from '../sub-rule-in-list-item.model';
import { SubRuleInListItemService } from '../service/sub-rule-in-list-item.service';
import { ISubRule } from 'app/entities/sub-rule/sub-rule.model';
import { SubRuleService } from 'app/entities/sub-rule/service/sub-rule.service';

@Component({
  selector: 'jhi-sub-rule-in-list-item-update',
  templateUrl: './sub-rule-in-list-item-update.component.html',
})
export class SubRuleInListItemUpdateComponent implements OnInit {
  isSaving = false;

  subRulesSharedCollection: ISubRule[] = [];

  editForm = this.fb.group({
    id: [],
    strItem: [],
    decItem: [],
    subRule: [],
  });

  constructor(
    protected subRuleInListItemService: SubRuleInListItemService,
    protected subRuleService: SubRuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subRuleInListItem }) => {
      this.updateForm(subRuleInListItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subRuleInListItem = this.createFromForm();
    if (subRuleInListItem.id !== undefined) {
      this.subscribeToSaveResponse(this.subRuleInListItemService.update(subRuleInListItem));
    } else {
      this.subscribeToSaveResponse(this.subRuleInListItemService.create(subRuleInListItem));
    }
  }

  trackSubRuleById(index: number, item: ISubRule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubRuleInListItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subRuleInListItem: ISubRuleInListItem): void {
    this.editForm.patchValue({
      id: subRuleInListItem.id,
      strItem: subRuleInListItem.strItem,
      decItem: subRuleInListItem.decItem,
      subRule: subRuleInListItem.subRule,
    });

    this.subRulesSharedCollection = this.subRuleService.addSubRuleToCollectionIfMissing(
      this.subRulesSharedCollection,
      subRuleInListItem.subRule
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subRuleService
      .query()
      .pipe(map((res: HttpResponse<ISubRule[]>) => res.body ?? []))
      .pipe(
        map((subRules: ISubRule[]) => this.subRuleService.addSubRuleToCollectionIfMissing(subRules, this.editForm.get('subRule')!.value))
      )
      .subscribe((subRules: ISubRule[]) => (this.subRulesSharedCollection = subRules));
  }

  protected createFromForm(): ISubRuleInListItem {
    return {
      ...new SubRuleInListItem(),
      id: this.editForm.get(['id'])!.value,
      strItem: this.editForm.get(['strItem'])!.value,
      decItem: this.editForm.get(['decItem'])!.value,
      subRule: this.editForm.get(['subRule'])!.value,
    };
  }
}
