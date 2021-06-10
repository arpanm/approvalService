import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprovalLevelStatus, getApprovalLevelStatusIdentifier } from '../approval-level-status.model';

export type EntityResponseType = HttpResponse<IApprovalLevelStatus>;
export type EntityArrayResponseType = HttpResponse<IApprovalLevelStatus[]>;

@Injectable({ providedIn: 'root' })
export class ApprovalLevelStatusService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/approval-level-statuses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(approvalLevelStatus: IApprovalLevelStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalLevelStatus);
    return this.http
      .post<IApprovalLevelStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(approvalLevelStatus: IApprovalLevelStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalLevelStatus);
    return this.http
      .put<IApprovalLevelStatus>(`${this.resourceUrl}/${getApprovalLevelStatusIdentifier(approvalLevelStatus) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(approvalLevelStatus: IApprovalLevelStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalLevelStatus);
    return this.http
      .patch<IApprovalLevelStatus>(`${this.resourceUrl}/${getApprovalLevelStatusIdentifier(approvalLevelStatus) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApprovalLevelStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApprovalLevelStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprovalLevelStatusToCollectionIfMissing(
    approvalLevelStatusCollection: IApprovalLevelStatus[],
    ...approvalLevelStatusesToCheck: (IApprovalLevelStatus | null | undefined)[]
  ): IApprovalLevelStatus[] {
    const approvalLevelStatuses: IApprovalLevelStatus[] = approvalLevelStatusesToCheck.filter(isPresent);
    if (approvalLevelStatuses.length > 0) {
      const approvalLevelStatusCollectionIdentifiers = approvalLevelStatusCollection.map(
        approvalLevelStatusItem => getApprovalLevelStatusIdentifier(approvalLevelStatusItem)!
      );
      const approvalLevelStatusesToAdd = approvalLevelStatuses.filter(approvalLevelStatusItem => {
        const approvalLevelStatusIdentifier = getApprovalLevelStatusIdentifier(approvalLevelStatusItem);
        if (approvalLevelStatusIdentifier == null || approvalLevelStatusCollectionIdentifiers.includes(approvalLevelStatusIdentifier)) {
          return false;
        }
        approvalLevelStatusCollectionIdentifiers.push(approvalLevelStatusIdentifier);
        return true;
      });
      return [...approvalLevelStatusesToAdd, ...approvalLevelStatusCollection];
    }
    return approvalLevelStatusCollection;
  }

  protected convertDateFromClient(approvalLevelStatus: IApprovalLevelStatus): IApprovalLevelStatus {
    return Object.assign({}, approvalLevelStatus, {
      createdAt: approvalLevelStatus.createdAt?.isValid() ? approvalLevelStatus.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: approvalLevelStatus.updatedAt?.isValid() ? approvalLevelStatus.updatedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((approvalLevelStatus: IApprovalLevelStatus) => {
        approvalLevelStatus.createdAt = approvalLevelStatus.createdAt ? dayjs(approvalLevelStatus.createdAt) : undefined;
        approvalLevelStatus.updatedAt = approvalLevelStatus.updatedAt ? dayjs(approvalLevelStatus.updatedAt) : undefined;
      });
    }
    return res;
  }
}
