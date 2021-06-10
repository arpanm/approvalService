import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubRule, SubRule } from '../sub-rule.model';
import { SubRuleService } from '../service/sub-rule.service';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalRuleService } from 'app/entities/approval-rule/service/approval-rule.service';

@Component({
  selector: 'jhi-sub-rule-update',
  templateUrl: './sub-rule-update.component.html',
})
export class SubRuleUpdateComponent implements OnInit {
  isSaving = false;

  approvalRulesSharedCollection: IApprovalRule[] = [];

  editForm = this.fb.group({
    id: [],
    fieldName: [null, [Validators.required]],
    dataType: [null, [Validators.required]],
    condition: [null, [Validators.required]],
    rangeMinValue: [],
    rangeMaxValue: [],
    equalStrValue: [],
    equalDecValue: [],
    appendType: [null, [Validators.required]],
    rule: [],
  });

  constructor(
    protected subRuleService: SubRuleService,
    protected approvalRuleService: ApprovalRuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subRule }) => {
      this.updateForm(subRule);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subRule = this.createFromForm();
    if (subRule.id !== undefined) {
      this.subscribeToSaveResponse(this.subRuleService.update(subRule));
    } else {
      this.subscribeToSaveResponse(this.subRuleService.create(subRule));
    }
  }

  trackApprovalRuleById(index: number, item: IApprovalRule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubRule>>): void {
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

  protected updateForm(subRule: ISubRule): void {
    this.editForm.patchValue({
      id: subRule.id,
      fieldName: subRule.fieldName,
      dataType: subRule.dataType,
      condition: subRule.condition,
      rangeMinValue: subRule.rangeMinValue,
      rangeMaxValue: subRule.rangeMaxValue,
      equalStrValue: subRule.equalStrValue,
      equalDecValue: subRule.equalDecValue,
      appendType: subRule.appendType,
      rule: subRule.rule,
    });

    this.approvalRulesSharedCollection = this.approvalRuleService.addApprovalRuleToCollectionIfMissing(
      this.approvalRulesSharedCollection,
      subRule.rule
    );
  }

  protected loadRelationshipsOptions(): void {
    this.approvalRuleService
      .query()
      .pipe(map((res: HttpResponse<IApprovalRule[]>) => res.body ?? []))
      .pipe(
        map((approvalRules: IApprovalRule[]) =>
          this.approvalRuleService.addApprovalRuleToCollectionIfMissing(approvalRules, this.editForm.get('rule')!.value)
        )
      )
      .subscribe((approvalRules: IApprovalRule[]) => (this.approvalRulesSharedCollection = approvalRules));
  }

  protected createFromForm(): ISubRule {
    return {
      ...new SubRule(),
      id: this.editForm.get(['id'])!.value,
      fieldName: this.editForm.get(['fieldName'])!.value,
      dataType: this.editForm.get(['dataType'])!.value,
      condition: this.editForm.get(['condition'])!.value,
      rangeMinValue: this.editForm.get(['rangeMinValue'])!.value,
      rangeMaxValue: this.editForm.get(['rangeMaxValue'])!.value,
      equalStrValue: this.editForm.get(['equalStrValue'])!.value,
      equalDecValue: this.editForm.get(['equalDecValue'])!.value,
      appendType: this.editForm.get(['appendType'])!.value,
      rule: this.editForm.get(['rule'])!.value,
    };
  }
}
