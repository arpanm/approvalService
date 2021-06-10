import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprovalRule, getApprovalRuleIdentifier } from '../approval-rule.model';

export type EntityResponseType = HttpResponse<IApprovalRule>;
export type EntityArrayResponseType = HttpResponse<IApprovalRule[]>;

@Injectable({ providedIn: 'root' })
export class ApprovalRuleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/approval-rules');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(approvalRule: IApprovalRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalRule);
    return this.http
      .post<IApprovalRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(approvalRule: IApprovalRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalRule);
    return this.http
      .put<IApprovalRule>(`${this.resourceUrl}/${getApprovalRuleIdentifier(approvalRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(approvalRule: IApprovalRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalRule);
    return this.http
      .patch<IApprovalRule>(`${this.resourceUrl}/${getApprovalRuleIdentifier(approvalRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApprovalRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApprovalRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprovalRuleToCollectionIfMissing(
    approvalRuleCollection: IApprovalRule[],
    ...approvalRulesToCheck: (IApprovalRule | null | undefined)[]
  ): IApprovalRule[] {
    const approvalRules: IApprovalRule[] = approvalRulesToCheck.filter(isPresent);
    if (approvalRules.length > 0) {
      const approvalRuleCollectionIdentifiers = approvalRuleCollection.map(
        approvalRuleItem => getApprovalRuleIdentifier(approvalRuleItem)!
      );
      const approvalRulesToAdd = approvalRules.filter(approvalRuleItem => {
        const approvalRuleIdentifier = getApprovalRuleIdentifier(approvalRuleItem);
        if (approvalRuleIdentifier == null || approvalRuleCollectionIdentifiers.includes(approvalRuleIdentifier)) {
          return false;
        }
        approvalRuleCollectionIdentifiers.push(approvalRuleIdentifier);
        return true;
      });
      return [...approvalRulesToAdd, ...approvalRuleCollection];
    }
    return approvalRuleCollection;
  }

  protected convertDateFromClient(approvalRule: IApprovalRule): IApprovalRule {
    return Object.assign({}, approvalRule, {
      createdAt: approvalRule.createdAt?.isValid() ? approvalRule.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: approvalRule.updatedAt?.isValid() ? approvalRule.updatedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((approvalRule: IApprovalRule) => {
        approvalRule.createdAt = approvalRule.createdAt ? dayjs(approvalRule.createdAt) : undefined;
        approvalRule.updatedAt = approvalRule.updatedAt ? dayjs(approvalRule.updatedAt) : undefined;
      });
    }
    return res;
  }
}
