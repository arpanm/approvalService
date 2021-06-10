import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprovalRequest, getApprovalRequestIdentifier } from '../approval-request.model';

export type EntityResponseType = HttpResponse<IApprovalRequest>;
export type EntityArrayResponseType = HttpResponse<IApprovalRequest[]>;

@Injectable({ providedIn: 'root' })
export class ApprovalRequestService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/approval-requests');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(approvalRequest: IApprovalRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalRequest);
    return this.http
      .post<IApprovalRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(approvalRequest: IApprovalRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalRequest);
    return this.http
      .put<IApprovalRequest>(`${this.resourceUrl}/${getApprovalRequestIdentifier(approvalRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(approvalRequest: IApprovalRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalRequest);
    return this.http
      .patch<IApprovalRequest>(`${this.resourceUrl}/${getApprovalRequestIdentifier(approvalRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApprovalRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApprovalRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprovalRequestToCollectionIfMissing(
    approvalRequestCollection: IApprovalRequest[],
    ...approvalRequestsToCheck: (IApprovalRequest | null | undefined)[]
  ): IApprovalRequest[] {
    const approvalRequests: IApprovalRequest[] = approvalRequestsToCheck.filter(isPresent);
    if (approvalRequests.length > 0) {
      const approvalRequestCollectionIdentifiers = approvalRequestCollection.map(
        approvalRequestItem => getApprovalRequestIdentifier(approvalRequestItem)!
      );
      const approvalRequestsToAdd = approvalRequests.filter(approvalRequestItem => {
        const approvalRequestIdentifier = getApprovalRequestIdentifier(approvalRequestItem);
        if (approvalRequestIdentifier == null || approvalRequestCollectionIdentifiers.includes(approvalRequestIdentifier)) {
          return false;
        }
        approvalRequestCollectionIdentifiers.push(approvalRequestIdentifier);
        return true;
      });
      return [...approvalRequestsToAdd, ...approvalRequestCollection];
    }
    return approvalRequestCollection;
  }

  protected convertDateFromClient(approvalRequest: IApprovalRequest): IApprovalRequest {
    return Object.assign({}, approvalRequest, {
      createdAt: approvalRequest.createdAt?.isValid() ? approvalRequest.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: approvalRequest.updatedAt?.isValid() ? approvalRequest.updatedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((approvalRequest: IApprovalRequest) => {
        approvalRequest.createdAt = approvalRequest.createdAt ? dayjs(approvalRequest.createdAt) : undefined;
        approvalRequest.updatedAt = approvalRequest.updatedAt ? dayjs(approvalRequest.updatedAt) : undefined;
      });
    }
    return res;
  }
}
