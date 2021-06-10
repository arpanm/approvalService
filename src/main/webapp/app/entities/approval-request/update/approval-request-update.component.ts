import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprovalRequest, ApprovalRequest } from '../approval-request.model';
import { ApprovalRequestService } from '../service/approval-request.service';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalRuleService } from 'app/entities/approval-rule/service/approval-rule.service';

@Component({
  selector: 'jhi-approval-request-update',
  templateUrl: './approval-request-update.component.html',
})
export class ApprovalRequestUpdateComponent implements OnInit {
  isSaving = false;

  approvalRulesSharedCollection: IApprovalRule[] = [];

  editForm = this.fb.group({
    id: [],
    programId: [null, [Validators.required]],
    type: [null, [Validators.required]],
    approveCallBackUrl: [],
    rejectCallBackUrl: [],
    createdBy: [],
    createdAt: [],
    updatedBy: [],
    updatedAt: [],
    finalStatus: [],
    rule: [],
  });

  constructor(
    protected approvalRequestService: ApprovalRequestService,
    protected approvalRuleService: ApprovalRuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalRequest }) => {
      this.updateForm(approvalRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvalRequest = this.createFromForm();
    if (approvalRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.approvalRequestService.update(approvalRequest));
    } else {
      this.subscribeToSaveResponse(this.approvalRequestService.create(approvalRequest));
    }
  }

  trackApprovalRuleById(index: number, item: IApprovalRule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovalRequest>>): void {
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

  protected updateForm(approvalRequest: IApprovalRequest): void {
    this.editForm.patchValue({
      id: approvalRequest.id,
      programId: approvalRequest.programId,
      type: approvalRequest.type,
      approveCallBackUrl: approvalRequest.approveCallBackUrl,
      rejectCallBackUrl: approvalRequest.rejectCallBackUrl,
      createdBy: approvalRequest.createdBy,
      createdAt: approvalRequest.createdAt,
      updatedBy: approvalRequest.updatedBy,
      updatedAt: approvalRequest.updatedAt,
      finalStatus: approvalRequest.finalStatus,
      rule: approvalRequest.rule,
    });

    this.approvalRulesSharedCollection = this.approvalRuleService.addApprovalRuleToCollectionIfMissing(
      this.approvalRulesSharedCollection,
      approvalRequest.rule
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

  protected createFromForm(): IApprovalRequest {
    return {
      ...new ApprovalRequest(),
      id: this.editForm.get(['id'])!.value,
      programId: this.editForm.get(['programId'])!.value,
      type: this.editForm.get(['type'])!.value,
      approveCallBackUrl: this.editForm.get(['approveCallBackUrl'])!.value,
      rejectCallBackUrl: this.editForm.get(['rejectCallBackUrl'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
      finalStatus: this.editForm.get(['finalStatus'])!.value,
      rule: this.editForm.get(['rule'])!.value,
    };
  }
}
