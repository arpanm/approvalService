import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprover, Approver } from '../approver.model';
import { ApproverService } from '../service/approver.service';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalRuleService } from 'app/entities/approval-rule/service/approval-rule.service';

@Component({
  selector: 'jhi-approver-update',
  templateUrl: './approver-update.component.html',
})
export class ApproverUpdateComponent implements OnInit {
  isSaving = false;

  approvalRulesSharedCollection: IApprovalRule[] = [];

  editForm = this.fb.group({
    id: [],
    programUserId: [null, [Validators.required]],
    email: [null, [Validators.required]],
    level: [null, [Validators.required]],
    rule: [],
  });

  constructor(
    protected approverService: ApproverService,
    protected approvalRuleService: ApprovalRuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approver }) => {
      this.updateForm(approver);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approver = this.createFromForm();
    if (approver.id !== undefined) {
      this.subscribeToSaveResponse(this.approverService.update(approver));
    } else {
      this.subscribeToSaveResponse(this.approverService.create(approver));
    }
  }

  trackApprovalRuleById(index: number, item: IApprovalRule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprover>>): void {
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

  protected updateForm(approver: IApprover): void {
    this.editForm.patchValue({
      id: approver.id,
      programUserId: approver.programUserId,
      email: approver.email,
      level: approver.level,
      rule: approver.rule,
    });

    this.approvalRulesSharedCollection = this.approvalRuleService.addApprovalRuleToCollectionIfMissing(
      this.approvalRulesSharedCollection,
      approver.rule
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

  protected createFromForm(): IApprover {
    return {
      ...new Approver(),
      id: this.editForm.get(['id'])!.value,
      programUserId: this.editForm.get(['programUserId'])!.value,
      email: this.editForm.get(['email'])!.value,
      level: this.editForm.get(['level'])!.value,
      rule: this.editForm.get(['rule'])!.value,
    };
  }
}
