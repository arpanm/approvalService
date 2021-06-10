import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalLevelStatus } from '../approval-level-status.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ApprovalLevelStatusService } from '../service/approval-level-status.service';
import { ApprovalLevelStatusDeleteDialogComponent } from '../delete/approval-level-status-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-approval-level-status',
  templateUrl: './approval-level-status.component.html',
})
export class ApprovalLevelStatusComponent implements OnInit {
  approvalLevelStatuses: IApprovalLevelStatus[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected approvalLevelStatusService: ApprovalLevelStatusService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.approvalLevelStatuses = [];
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

    this.approvalLevelStatusService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IApprovalLevelStatus[]>) => {
          this.isLoading = false;
          this.paginateApprovalLevelStatuses(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.approvalLevelStatuses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IApprovalLevelStatus): number {
    return item.id!;
  }

  delete(approvalLevelStatus: IApprovalLevelStatus): void {
    const modalRef = this.modalService.open(ApprovalLevelStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvalLevelStatus = approvalLevelStatus;
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

  protected paginateApprovalLevelStatuses(data: IApprovalLevelStatus[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.approvalLevelStatuses.push(d);
      }
    }
  }
}
