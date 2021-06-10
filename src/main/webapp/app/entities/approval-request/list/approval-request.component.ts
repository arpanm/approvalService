import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalRequest } from '../approval-request.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ApprovalRequestService } from '../service/approval-request.service';
import { ApprovalRequestDeleteDialogComponent } from '../delete/approval-request-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-approval-request',
  templateUrl: './approval-request.component.html',
})
export class ApprovalRequestComponent implements OnInit {
  approvalRequests: IApprovalRequest[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected approvalRequestService: ApprovalRequestService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.approvalRequests = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.approvalRequestService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IApprovalRequest[]>) => {
          this.isLoading = false;
          this.paginateApprovalRequests(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.approvalRequests = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IApprovalRequest): number {
    return item.id!;
  }

  delete(approvalRequest: IApprovalRequest): void {
    const modalRef = this.modalService.open(ApprovalRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvalRequest = approvalRequest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateApprovalRequests(data: IApprovalRequest[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.approvalRequests.push(d);
      }
    }
  }
}
