import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprovalLevelStatus, ApprovalLevelStatus } from '../approval-level-status.model';
import { ApprovalLevelStatusService } from '../service/approval-level-status.service';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalRequestService } from 'app/entities/approval-request/service/approval-request.service';
import { IApprover } from 'app/entities/approver/approver.model';
import { ApproverService } from 'app/entities/approver/service/approver.service';

@Component({
  selector: 'jhi-approval-level-status-update',
  templateUrl: './approval-level-status-update.component.html',
})
export class ApprovalLevelStatusUpdateComponent implements OnInit {
  isSaving = false;

  approvalRequestsSharedCollection: IApprovalRequest[] = [];
  approversSharedCollection: IApprover[] = [];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    level: [null, [Validators.required]],
    clientTime: [null, [Validators.required]],
    createdBy: [],
    createdAt: [],
    updatedBy: [],
    updatedAt: [],
    request: [],
    approver: [],
  });

  constructor(
    protected approvalLevelStatusService: ApprovalLevelStatusService,
    protected approvalRequestService: ApprovalRequestService,
    protected approverService: ApproverService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalLevelStatus }) => {
      this.updateForm(approvalLevelStatus);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvalLevelStatus = this.createFromForm();
    if (approvalLevelStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.approvalLevelStatusService.update(approvalLevelStatus));
    } else {
      this.subscribeToSaveResponse(this.approvalLevelStatusService.create(approvalLevelStatus));
    }
  }

  trackApprovalRequestById(index: number, item: IApprovalRequest): number {
    return item.id!;
  }

  trackApproverById(index: number, item: IApprover): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovalLevelStatus>>): void {
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

  protected updateForm(approvalLevelStatus: IApprovalLevelStatus): void {
    this.editForm.patchValue({
      id: approvalLevelStatus.id,
      status: approvalLevelStatus.status,
      level: approvalLevelStatus.level,
      clientTime: approvalLevelStatus.clientTime,
      createdBy: approvalLevelStatus.createdBy,
      createdAt: approvalLevelStatus.createdAt,
      updatedBy: approvalLevelStatus.updatedBy,
      updatedAt: approvalLevelStatus.updatedAt,
      request: approvalLevelStatus.request,
      approver: approvalLevelStatus.approver,
    });

    this.approvalRequestsSharedCollection = this.approvalRequestService.addApprovalRequestToCollectionIfMissing(
      this.approvalRequestsSharedCollection,
      approvalLevelStatus.request
    );
    this.approversSharedCollection = this.approverService.addApproverToCollectionIfMissing(
      this.approversSharedCollection,
      approvalLevelStatus.approver
    );
  }

  protected loadRelationshipsOptions(): void {
    this.approvalRequestService
      .query()
      .pipe(map((res: HttpResponse<IApprovalRequest[]>) => res.body ?? []))
      .pipe(
        map((approvalRequests: IApprovalRequest[]) =>
          this.approvalRequestService.addApprovalRequestToCollectionIfMissing(approvalRequests, this.editForm.get('request')!.value)
        )
      )
      .subscribe((approvalRequests: IApprovalRequest[]) => (this.approvalRequestsSharedCollection = approvalRequests));

    this.approverService
      .query()
      .pipe(map((res: HttpResponse<IApprover[]>) => res.body ?? []))
      .pipe(
        map((approvers: IApprover[]) =>
          this.approverService.addApproverToCollectionIfMissing(approvers, this.editForm.get('approver')!.value)
        )
      )
      .subscribe((approvers: IApprover[]) => (this.approversSharedCollection = approvers));
  }

  protected createFromForm(): IApprovalLevelStatus {
    return {
      ...new ApprovalLevelStatus(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      level: this.editForm.get(['level'])!.value,
      clientTime: this.editForm.get(['clientTime'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
      request: this.editForm.get(['request'])!.value,
      approver: this.editForm.get(['approver'])!.value,
    };
  }
}
