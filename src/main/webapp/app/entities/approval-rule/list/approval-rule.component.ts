import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalRule } from '../approval-rule.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ApprovalRuleService } from '../service/approval-rule.service';
import { ApprovalRuleDeleteDialogComponent } from '../delete/approval-rule-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-approval-rule',
  templateUrl: './approval-rule.component.html',
})
export class ApprovalRuleComponent implements OnInit {
  approvalRules: IApprovalRule[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected approvalRuleService: ApprovalRuleService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.approvalRules = [];
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

    this.approvalRuleService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IApprovalRule[]>) => {
          this.isLoading = false;
          this.paginateApprovalRules(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.approvalRules = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IApprovalRule): number {
    return item.id!;
  }

  delete(approvalRule: IApprovalRule): void {
    const modalRef = this.modalService.open(ApprovalRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvalRule = approvalRule;
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

  protected paginateApprovalRules(data: IApprovalRule[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.approvalRules.push(d);
      }
    }
  }
}
