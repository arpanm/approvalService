import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApprovalRule, ApprovalRule } from '../approval-rule.model';
import { ApprovalRuleService } from '../service/approval-rule.service';

@Component({
  selector: 'jhi-approval-rule-update',
  templateUrl: './approval-rule-update.component.html',
})
export class ApprovalRuleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    programId: [null, [Validators.required]],
    type: [null, [Validators.required]],
    createdBy: [],
    createdAt: [],
    updatedBy: [],
    updatedAt: [],
  });

  constructor(protected approvalRuleService: ApprovalRuleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalRule }) => {
      this.updateForm(approvalRule);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvalRule = this.createFromForm();
    if (approvalRule.id !== undefined) {
      this.subscribeToSaveResponse(this.approvalRuleService.update(approvalRule));
    } else {
      this.subscribeToSaveResponse(this.approvalRuleService.create(approvalRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovalRule>>): void {
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

  protected updateForm(approvalRule: IApprovalRule): void {
    this.editForm.patchValue({
      id: approvalRule.id,
      programId: approvalRule.programId,
      type: approvalRule.type,
      createdBy: approvalRule.createdBy,
      createdAt: approvalRule.createdAt,
      updatedBy: approvalRule.updatedBy,
      updatedAt: approvalRule.updatedAt,
    });
  }

  protected createFromForm(): IApprovalRule {
    return {
      ...new ApprovalRule(),
      id: this.editForm.get(['id'])!.value,
      programId: this.editForm.get(['programId'])!.value,
      type: this.editForm.get(['type'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
    };
  }
}
