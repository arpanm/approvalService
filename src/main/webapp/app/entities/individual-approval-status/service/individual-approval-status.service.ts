import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIndividualApprovalStatus, getIndividualApprovalStatusIdentifier } from '../individual-approval-status.model';

export type EntityResponseType = HttpResponse<IIndividualApprovalStatus>;
export type EntityArrayResponseType = HttpResponse<IIndividualApprovalStatus[]>;

@Injectable({ providedIn: 'root' })
export class IndividualApprovalStatusService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/individual-approval-statuses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(individualApprovalStatus: IIndividualApprovalStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individualApprovalStatus);
    return this.http
      .post<IIndividualApprovalStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(individualApprovalStatus: IIndividualApprovalStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individualApprovalStatus);
    return this.http
      .put<IIndividualApprovalStatus>(
        `${this.resourceUrl}/${getIndividualApprovalStatusIdentifier(individualApprovalStatus) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(individualApprovalStatus: IIndividualApprovalStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individualApprovalStatus);
    return this.http
      .patch<IIndividualApprovalStatus>(
        `${this.resourceUrl}/${getIndividualApprovalStatusIdentifier(individualApprovalStatus) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIndividualApprovalStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIndividualApprovalStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIndividualApprovalStatusToCollectionIfMissing(
    individualApprovalStatusCollection: IIndividualApprovalStatus[],
    ...individualApprovalStatusesToCheck: (IIndividualApprovalStatus | null | undefined)[]
  ): IIndividualApprovalStatus[] {
    const individualApprovalStatuses: IIndividualApprovalStatus[] = individualApprovalStatusesToCheck.filter(isPresent);
    if (individualApprovalStatuses.length > 0) {
      const individualApprovalStatusCollectionIdentifiers = individualApprovalStatusCollection.map(
        individualApprovalStatusItem => getIndividualApprovalStatusIdentifier(individualApprovalStatusItem)!
      );
      const individualApprovalStatusesToAdd = individualApprovalStatuses.filter(individualApprovalStatusItem => {
        const individualApprovalStatusIdentifier = getIndividualApprovalStatusIdentifier(individualApprovalStatusItem);
        if (
          individualApprovalStatusIdentifier == null ||
          individualApprovalStatusCollectionIdentifiers.includes(individualApprovalStatusIdentifier)
        ) {
          return false;
        }
        individualApprovalStatusCollectionIdentifiers.push(individualApprovalStatusIdentifier);
        return true;
      });
      return [...individualApprovalStatusesToAdd, ...individualApprovalStatusCollection];
    }
    return individualApprovalStatusCollection;
  }

  protected convertDateFromClient(individualApprovalStatus: IIndividualApprovalStatus): IIndividualApprovalStatus {
    return Object.assign({}, individualApprovalStatus, {
      createdAt: individualApprovalStatus.createdAt?.isValid() ? individualApprovalStatus.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: individualApprovalStatus.updatedAt?.isValid() ? individualApprovalStatus.updatedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((individualApprovalStatus: IIndividualApprovalStatus) => {
        individualApprovalStatus.createdAt = individualApprovalStatus.createdAt ? dayjs(individualApprovalStatus.createdAt) : undefined;
        individualApprovalStatus.updatedAt = individualApprovalStatus.updatedAt ? dayjs(individualApprovalStatus.updatedAt) : undefined;
      });
    }
    return res;
  }
}
