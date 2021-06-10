import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIndividualApprovalStatus, IndividualApprovalStatus } from '../individual-approval-status.model';
import { IndividualApprovalStatusService } from '../service/individual-approval-status.service';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalRequestService } from 'app/entities/approval-request/service/approval-request.service';
import { IApprover } from 'app/entities/approver/approver.model';
import { ApproverService } from 'app/entities/approver/service/approver.service';

@Component({
  selector: 'jhi-individual-approval-status-update',
  templateUrl: './individual-approval-status-update.component.html',
})
export class IndividualApprovalStatusUpdateComponent implements OnInit {
  isSaving = false;

  approvalRequestsSharedCollection: IApprovalRequest[] = [];
  approversSharedCollection: IApprover[] = [];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    clientTime: [null, [Validators.required]],
    createdBy: [],
    createdAt: [],
    updatedBy: [],
    updatedAt: [],
    request: [],
    approver: [],
  });

  constructor(
    protected individualApprovalStatusService: IndividualApprovalStatusService,
    protected approvalRequestService: ApprovalRequestService,
    protected approverService: ApproverService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ individualApprovalStatus }) => {
      this.updateForm(individualApprovalStatus);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const individualApprovalStatus = this.createFromForm();
    if (individualApprovalStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.individualApprovalStatusService.update(individualApprovalStatus));
    } else {
      this.subscribeToSaveResponse(this.individualApprovalStatusService.create(individualApprovalStatus));
    }
  }

  trackApprovalRequestById(index: number, item: IApprovalRequest): number {
    return item.id!;
  }

  trackApproverById(index: number, item: IApprover): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndividualApprovalStatus>>): void {
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

  protected updateForm(individualApprovalStatus: IIndividualApprovalStatus): void {
    this.editForm.patchValue({
      id: individualApprovalStatus.id,
      status: individualApprovalStatus.status,
      clientTime: individualApprovalStatus.clientTime,
      createdBy: individualApprovalStatus.createdBy,
      createdAt: individualApprovalStatus.createdAt,
      updatedBy: individualApprovalStatus.updatedBy,
      updatedAt: individualApprovalStatus.updatedAt,
      request: individualApprovalStatus.request,
      approver: individualApprovalStatus.approver,
    });

    this.approvalRequestsSharedCollection = this.approvalRequestService.addApprovalRequestToCollectionIfMissing(
      this.approvalRequestsSharedCollection,
      individualApprovalStatus.request
    );
    this.approversSharedCollection = this.approverService.addApproverToCollectionIfMissing(
      this.approversSharedCollection,
      individualApprovalStatus.approver
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

  protected createFromForm(): IIndividualApprovalStatus {
    return {
      ...new IndividualApprovalStatus(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
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
