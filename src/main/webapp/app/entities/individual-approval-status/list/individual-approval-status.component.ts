import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIndividualApprovalStatus } from '../individual-approval-status.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { IndividualApprovalStatusService } from '../service/individual-approval-status.service';
import { IndividualApprovalStatusDeleteDialogComponent } from '../delete/individual-approval-status-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-individual-approval-status',
  templateUrl: './individual-approval-status.component.html',
})
export class IndividualApprovalStatusComponent implements OnInit {
  individualApprovalStatuses: IIndividualApprovalStatus[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected individualApprovalStatusService: IndividualApprovalStatusService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.individualApprovalStatuses = [];
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

    this.individualApprovalStatusService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IIndividualApprovalStatus[]>) => {
          this.isLoading = false;
          this.paginateIndividualApprovalStatuses(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.individualApprovalStatuses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIndividualApprovalStatus): number {
    return item.id!;
  }

  delete(individualApprovalStatus: IIndividualApprovalStatus): void {
    const modalRef = this.modalService.open(IndividualApprovalStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.individualApprovalStatus = individualApprovalStatus;
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

  protected paginateIndividualApprovalStatuses(data: IIndividualApprovalStatus[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.individualApprovalStatuses.push(d);
      }
    }
  }
}
