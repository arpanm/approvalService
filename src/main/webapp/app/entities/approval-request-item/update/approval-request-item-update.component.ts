import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprovalRequestItem, ApprovalRequestItem } from '../approval-request-item.model';
import { ApprovalRequestItemService } from '../service/approval-request-item.service';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalRequestService } from 'app/entities/approval-request/service/approval-request.service';

@Component({
  selector: 'jhi-approval-request-item-update',
  templateUrl: './approval-request-item-update.component.html',
})
export class ApprovalRequestItemUpdateComponent implements OnInit {
  isSaving = false;

  approvalRequestsSharedCollection: IApprovalRequest[] = [];

  editForm = this.fb.group({
    id: [],
    fieldName: [null, [Validators.required]],
    dataType: [null, [Validators.required]],
    strValue: [],
    decValue: [],
    request: [],
  });

  constructor(
    protected approvalRequestItemService: ApprovalRequestItemService,
    protected approvalRequestService: ApprovalRequestService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvalRequestItem }) => {
      this.updateForm(approvalRequestItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvalRequestItem = this.createFromForm();
    if (approvalRequestItem.id !== undefined) {
      this.subscribeToSaveResponse(this.approvalRequestItemService.update(approvalRequestItem));
    } else {
      this.subscribeToSaveResponse(this.approvalRequestItemService.create(approvalRequestItem));
    }
  }

  trackApprovalRequestById(index: number, item: IApprovalRequest): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovalRequestItem>>): void {
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

  protected updateForm(approvalRequestItem: IApprovalRequestItem): void {
    this.editForm.patchValue({
      id: approvalRequestItem.id,
      fieldName: approvalRequestItem.fieldName,
      dataType: approvalRequestItem.dataType,
      strValue: approvalRequestItem.strValue,
      decValue: approvalRequestItem.decValue,
      request: approvalRequestItem.request,
    });

    this.approvalRequestsSharedCollection = this.approvalRequestService.addApprovalRequestToCollectionIfMissing(
      this.approvalRequestsSharedCollection,
      approvalRequestItem.request
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
  }

  protected createFromForm(): IApprovalRequestItem {
    return {
      ...new ApprovalRequestItem(),
      id: this.editForm.get(['id'])!.value,
      fieldName: this.editForm.get(['fieldName'])!.value,
      dataType: this.editForm.get(['dataType'])!.value,
      strValue: this.editForm.get(['strValue'])!.value,
      decValue: this.editForm.get(['decValue'])!.value,
      request: this.editForm.get(['request'])!.value,
    };
  }
}
